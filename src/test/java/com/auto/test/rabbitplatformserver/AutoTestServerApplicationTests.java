package com.auto.test.rabbitplatformserver;

import com.auto.test.config.apiggs.CostomPostmanRender;
import com.auto.test.entity.TAutoInterface;
import com.auto.test.entity.TAutoInterfaceClassify;
import com.auto.test.service.TAutoInterfaceClassifyService;
import com.auto.test.service.TAutoInterfaceService;
import com.github.apigcc.core.Apigcc;
import com.github.apigcc.core.Context;
import com.github.apigcc.core.common.postman.Folder;
import com.github.apigcc.core.common.postman.Postman;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutoTestServerApplicationTests {

    @Resource
    private TAutoInterfaceService interfaceService;
    
    @Resource
    TAutoInterfaceClassifyService classifyService;

    @Test
    public void contextLoads() {
    
        Context context = new Context();
        context.setId("ava");
        context.setName("AVA");
        context.addSource(Paths.get("E:/project/idea/deploy/fast_deploy/"));
    
        Apigcc apigcc = new Apigcc(context);
        apigcc.parse();
        apigcc.render();
        CostomPostmanRender costomPostmanTreeHandler = new CostomPostmanRender();
    
        Postman postman =costomPostmanTreeHandler.build(apigcc.getProject());
    
        List<TAutoInterface> list = new ArrayList<>();
        List<Folder>  postmanItems = postman.getItem();
        if(postmanItems!=null){
            postmanItems.stream().forEach(postmanItem -> {
                TAutoInterfaceClassify interfaceClassify = new TAutoInterfaceClassify();
                interfaceClassify.setName(postmanItem.getName());
                classifyService.save(interfaceClassify);
                List<Folder> items = postmanItem.getItem();
                if(items!=null&&items.size()>0){
                    items.stream().forEach(postmanItemSubitem -> {
                        TAutoInterface autoInterface = new TAutoInterface(postmanItemSubitem);
                        autoInterface.setClassifyId(interfaceClassify.getId());
                        interfaceService.save(autoInterface);
                    });
                }
                return;
            
            });
        }
       
    }
  
}

