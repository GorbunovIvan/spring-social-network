package org.example.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MySpringMVCDispatcherServletInitializer {//extends AbstractAnnotationConfigDispatcherServletInitializer {

//    @Override
//    protected Class<?>[] getRootConfigClasses() {
//        return null;
//    }
//
//    @Override
//    protected Class<?>[] getServletConfigClasses() {
//        return new Class[] {SpringConfig.class};
//    }
//
//    @Override
//    protected String[] getServletMappings() {
//        return new String[] {"/"};
//    }
//
//    @Override
//    public void onStartup(ServletContext aServletContext) throws ServletException {
//        super.onStartup(aServletContext);
//        registerHiddenFieldFilter(aServletContext);
//    }
//
//    private void registerHiddenFieldFilter(ServletContext aContext) {
//        aContext.addFilter("hiddenHttpMethodFilter",
//                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
//    }
}
