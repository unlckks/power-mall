package com.mingyun.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 11:06
 * 请求日志的filter
 */
@Component
@Slf4j
public class QpsRecordsFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String ip = request.getRemoteAddress().getHostString();
        String api = request.getURI().getPath();
        log.info("请求时间:{},ip为:{},api:{}", new Date(), ip, api);
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return -10;
    }
}
