package com.advancestores.centralservices.demo.resources;

import java.util.List;

import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.jbpm.services.api.DeploymentService;
import org.jbpm.services.cdi.Kjar;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.internal.identity.IdentityProvider;
import org.kie.internal.runtime.manager.cdi.qualifier.PerProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.PerRequest;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;

/**
 * 
 * <p>
 * File: Resources.java
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
public class Resources
{
    @PersistenceUnit(unitName = "org.jbpm.domain")
    private EntityManagerFactory emf;

    @Inject
    @Kjar
    private DeploymentService    deploymentService;

    @Produces
    public EntityManagerFactory getEntityManagerFactory()
    {
        return this.emf;
    }

    @Produces
    public DeploymentService getDeploymentService()
    {
        return this.deploymentService;
    }

    @Produces
    public IdentityProvider produceIdentityProvider()
    {
        return new IdentityProvider()
        {
            @Override
            public String getName()
            {
                return null;
            }

            // @Override
            public List<String> getRoles()
            {
                return null;
            }

            @Override
            public boolean hasRole(String arg0)
            {
                return false;
            }
        };
    }

    @Produces
    @PerRequest
    @PerProcessInstance
    @Singleton
    public RuntimeEnvironment produceEnvironment()
    {
        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get()
                .newClasspathKmoduleDefaultBuilder("helloBase", "hello").entityManagerFactory(emf).get();
        // .userGroupCallback(produceSelectedUserGroupCalback()).get();
        return environment;
    }
}
