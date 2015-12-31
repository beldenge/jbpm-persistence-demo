package com.advancestores.centralservices.demo.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.drools.compiler.kie.builder.impl.KieServicesImpl;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;
import org.kie.internal.runtime.manager.context.EmptyContext;

/**
 * 
 * <p>
 * File: BusinessProcessService.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Developers:
 * <ul>
 * <li>@author george.belden</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Last Modified By: @author $Author: george.belden $
 * </p>
 * 
 * @version $Revision: 13566 $
 * 
 *          Copyright (c) 2015 Advance Stores, Inc. All Rights Reserved
 */
@javax.ejb.Singleton
@Startup
@Path("")
public class BusinessProcessService
{
    @Inject
    @Singleton
    private RuntimeManager runtimeManager;

    private KieSession     ksession;

    @PostConstruct
    public void configure()
    {
        RuntimeEngine runtime = runtimeManager.getRuntimeEngine(EmptyContext.get());
        ksession = runtime.getKieSession();
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello()
    {
        ProcessInstance processInstance = ksession.startProcess("hello.helloWorld", null);

        return Response.status(Status.OK).entity("Process ID: " + processInstance.getId()).build();
    }

    @GET
    @Path("/timedHello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response timedHello()
    {
        ProcessInstance processInstance = ksession.startProcess("hello.timedHello", null);

        return Response.status(Status.OK).entity("Process ID: " + processInstance.getId()).build();
    }

    @PreDestroy
    public void shutdown()
    {
        if (runtimeManager != null)
        {
            runtimeManager.disposeRuntimeEngine(runtimeManager.getRuntimeEngine(EmptyContext.get()));
            runtimeManager.close();

            ((KieServicesImpl) KieServices.Factory.get()).nullKieClasspathContainer();
        }
    }
}
