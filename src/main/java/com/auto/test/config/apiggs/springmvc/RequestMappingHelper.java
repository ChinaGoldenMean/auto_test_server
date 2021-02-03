package com.auto.test.config.apiggs.springmvc;

import com.github.apigcc.core.common.URI;
import com.github.apigcc.core.common.helper.AnnotationHelper;
import com.github.apigcc.core.common.helper.ClassHelper;
import com.github.apigcc.core.common.helper.ExpressionHelper;
import com.github.apigcc.core.schema.Method;
import com.github.apigcc.core.springmvc.SpringParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

public class RequestMappingHelper {


    public static final String ANNOTATION_GET_MAPPING = "GetMapping";
    public static final String ANNOTATION_POST_MAPPING = "PostMapping";
    public static final String ANNOTATION_PUT_MAPPING = "PutMapping";
    public static final String ANNOTATION_PATCH_MAPPING = "PatchMapping";
    public static final String ANNOTATION_DELETE_MAPPING = "DeleteMapping";
    public static final String ANNOTATION_REQUEST_MAPPING = "RequestMapping";

    public static final String ANNOTATION_RESPONSE_BODY = "ResponseBody";

    public static final List<String> ANNOTATION_REQUEST_MAPPINGS = Lists.newArrayList(ANNOTATION_GET_MAPPING,
            ANNOTATION_POST_MAPPING, ANNOTATION_PUT_MAPPING, ANNOTATION_PATCH_MAPPING, ANNOTATION_DELETE_MAPPING, ANNOTATION_REQUEST_MAPPING);

    /**
     * 判断是否为Rest接口
     * @param n
     * @return
     */
    public static boolean isRest(MethodDeclaration n){
        if(n.isAnnotationPresent(ANNOTATION_RESPONSE_BODY)){
            return true;
        }
        Optional<Node> parentOptional = n.getParentNode();
        if(parentOptional.isPresent()){
            Node parentNode = parentOptional.get();
            if(parentNode instanceof ClassOrInterfaceDeclaration){
                if (((ClassOrInterfaceDeclaration) parentNode).isAnnotationPresent(SpringParser.ANNOTATION_REST_CONTROLLER)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解析HTTP Method
     * @param n
     * @return
     */
    public static Method pickMethod(MethodDeclaration n){
        if(n.isAnnotationPresent(ANNOTATION_GET_MAPPING)){
            return Method.GET;
        }
        if(n.isAnnotationPresent(ANNOTATION_POST_MAPPING)){
            return Method.POST;
        }
        if(n.isAnnotationPresent(ANNOTATION_PUT_MAPPING)){
            return Method.PUT;
        }
        if(n.isAnnotationPresent(ANNOTATION_PATCH_MAPPING)){
            return Method.PATCH;
        }
        if(n.isAnnotationPresent(ANNOTATION_DELETE_MAPPING)){
            return Method.DELETE;
        }
        if(n.isAnnotationPresent(ANNOTATION_REQUEST_MAPPING)){
            AnnotationExpr annotationExpr = n.getAnnotationByName(ANNOTATION_REQUEST_MAPPING).get();
            Optional<Expression> expressionOptional = AnnotationHelper.attr(annotationExpr, "method");
            if (expressionOptional.isPresent()) {
                Expression expression = expressionOptional.get();
                if(expression.isArrayInitializerExpr()){
                    NodeList<Expression> values = expression.asArrayInitializerExpr().getValues();
                    if(values!=null&&values.size()>0){
                        return Method.valueOf(values.get(0).toString().replaceAll("RequestMethod.",""));
                    }
                }
                return Method.of(expression.toString().replaceAll("RequestMethod.",""));
            }
        }
        return Method.GET;
    }


    /**
     * 递归获取父类和子类的 uri
     * @param n
     * @return
     */
    public static URI pickUriToParent(ClassOrInterfaceDeclaration n){
        URI parentUri = null;
        Optional<ClassOrInterfaceDeclaration> parentOptional = ClassHelper.getParent(n);
        if (parentOptional.isPresent()) {
            parentUri = pickUriToParent(parentOptional.get());
        }
        URI uri = new URI(pickUri(n.getAnnotations()));
        if(parentUri!=null){
            parentUri.add(uri);
            return parentUri;
        }
        return uri;
    }

    /**
     * 获取uri数据，有多个时，暂时只取第一个
     * @param nodeList
     * @return
     */
    public static String pickUri(NodeList<AnnotationExpr> nodeList){
        for (AnnotationExpr annotationExpr : nodeList) {
            if(ANNOTATION_REQUEST_MAPPINGS.contains(annotationExpr.getNameAsString())){
                Optional<Expression> expressionOptional = AnnotationHelper.attr(annotationExpr, "value","path");
                if (expressionOptional.isPresent()) {
                    Expression expression = expressionOptional.get();
                    return ExpressionHelper.getStringValue(expression);
                }
            }
        }
        return "";
    }

    /**
     * 获取headers
     * @param nodeList
     * @return
     */
    public static List<String> pickHeaders(NodeList<AnnotationExpr> nodeList){
        for (AnnotationExpr annotationExpr : nodeList) {
            if(ANNOTATION_REQUEST_MAPPINGS.contains(annotationExpr.getNameAsString())){
                Optional<Expression> expressionOptional = AnnotationHelper.attr(annotationExpr, "headers");
                if (expressionOptional.isPresent()) {
                    Expression expression = expressionOptional.get();
                    return ExpressionHelper.getStringValues(expression);
                }
            }
        }
        return Lists.newArrayList();
    }

    /**
     * 获取headers
     * @param nodeList
     * @return
     */
    public static List<String> pickConsumers(NodeList<AnnotationExpr> nodeList){
        for (AnnotationExpr annotationExpr : nodeList) {
            if(ANNOTATION_REQUEST_MAPPINGS.contains(annotationExpr.getNameAsString())){
                Optional<Expression> expressionOptional = AnnotationHelper.attr(annotationExpr, "consumes");
                if (expressionOptional.isPresent()) {
                    Expression expression = expressionOptional.get();
                    return ExpressionHelper.getStringValues(expression);
                }
            }
        }
        return Lists.newArrayList();
    }

    /**
     * 获取headers
     * @param nodeList
     * @return
     */
    public static List<String> pickProduces(NodeList<AnnotationExpr> nodeList){
        for (AnnotationExpr annotationExpr : nodeList) {
            if(ANNOTATION_REQUEST_MAPPINGS.contains(annotationExpr.getNameAsString())){
                Optional<Expression> expressionOptional = AnnotationHelper.attr(annotationExpr, "produces");
                if (expressionOptional.isPresent()) {
                    Expression expression = expressionOptional.get();
                    return ExpressionHelper.getStringValues(expression);
                }
            }
        }
        return Lists.newArrayList();
    }

}
