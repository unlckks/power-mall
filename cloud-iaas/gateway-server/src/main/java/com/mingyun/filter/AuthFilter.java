package com.mingyun.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mingyun.config.WhiteUrlsConfig;
import com.mingyun.constant.AuthConstant;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.constant.HttpConstant;
import com.mingyun.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @Author: MingYun
 * @Date: 2023-04-01 11:05
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter , Ordered {

    @Autowired
    private WhiteUrlsConfig whiteUrlsConfig ;

    @Autowired
    private RedisTemplate redisTemplate ;

    /**
     *与前端约定 token携带在header中 Authorization:bearer
     *1.先拿到请求的路经
     * 2.判断放行
     * 3.取token
     * 4.校验拦截
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (whiteUrlsConfig.getAllowUrls().contains(path)){
            return chain.filter(exchange);
        }
        //进行拿token
        String authorization =request.getHeaders().getFirst(AuthConstant.AUTHORIZATION);
        if (StringUtils.hasText(authorization)){
            //进行分割
            String token =authorization.replaceFirst(AuthConstant.BEARER,"");
            if (StringUtils.hasText(token) && redisTemplate.hasKey(AuthConstant.LOGIN_TOKEN_PREFIX+token)){
                return  chain.filter(exchange);
            }
        }
        log.error("网关拦截请求,api为:{}",path);
        //进行拦截
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set(HttpConstant.CONTENT_TYPE,HttpConstant.APPLICATION_JSON);
        //进行业务返回 200
        Result<Object> result = Result.fail(BusinessEnum.UN_AUTHORIZATION);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(wrap));
    }

    @Override
    public int getOrder() {
        return -5;
    }
}
