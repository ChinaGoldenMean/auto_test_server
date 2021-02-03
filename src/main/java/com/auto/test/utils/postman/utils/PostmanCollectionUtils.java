//package com.auto.test.utils.postman.utils;
//
//import com.auto.test.model.postman.PostmanCollection;
//import com.auto.test.model.postman.PostmanItem;
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//public class PostmanCollectionUtils {
//
//    private static void getItem(PostmanItem item, List<PostmanItem> itemList) {
//        if (item.getItems().size() == 0 && item.getRequest() != null) {
//            itemList.add(item);
//        }
//
//        for (PostmanItem i : item.getItems()) {
//            getItem(i, itemList);
//        }
//    }
//
//    public static List<PostmanItem> getItems(PostmanCollection postmanCollection) {
//        List<PostmanItem> items = new ArrayList<>();
//
//        for (PostmanItem item : postmanCollection.getItems()) {
//            getItem(item, items);
//        }
//        return items;
//    }
//}
