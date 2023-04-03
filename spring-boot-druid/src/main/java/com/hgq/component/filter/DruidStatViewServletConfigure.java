package com.hgq.component.filter;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: DruidStatViewServletConfigure
 * @Description: Druid monitoring 数据库连接池监控配置
 * @Auther: hgq
 * @Data: 2022年4月2日14:42:28
 */
@Configuration
public class DruidStatViewServletConfigure {


    @Bean
    public ServletRegistrationBean druidServlet(){
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("allow","127.0.0.1");// IP白名单 (没有配置或者为空，则允许所有访问,可配置多个)
        reg.addInitParameter("deny", "");//@WebInitParam(name="deny",value="192.168.16.111"),// IP黑名单 (存在共同时，deny优先于allow)
        reg.addInitParameter("loginUsername", "admin");
        reg.addInitParameter("loginPassword", "admin");
        reg.addInitParameter("resetEnable", "true");// 禁用HTML页面上的“Reset All”功能
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new WebStatFilter());
        filter.addUrlPatterns("/*");
        /**
         * 忽略资源
         */
        filter.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/LogServer/druid/");
        return filter;
    }
}
