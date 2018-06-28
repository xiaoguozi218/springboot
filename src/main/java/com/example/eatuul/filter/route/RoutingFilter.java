package com.example.eatuul.filter.route;

import com.example.eatuul.filter.EatuulFilter;
import com.example.eatuul.http.RequestContext;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by MintQ on 2018/6/28.
 * 这个是routeFilter,这里我偷懒了，直接做转发请求，并且将返回值ResponseEntity放入全局threadlocal中
 *
 */
public class RoutingFilter extends EatuulFilter {

    @Override
    public String filterType() {
        // TODO Auto-generated method stub
        return "route";
    }
    @Override
    public int filterOrder() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void run(){
        RequestContext ctx = RequestContext.getCurrentContext();
        RequestEntity requestEntity = ctx.getRequestEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,byte[].class);
        ctx.setResponseEntity(responseEntity);
    }

}
