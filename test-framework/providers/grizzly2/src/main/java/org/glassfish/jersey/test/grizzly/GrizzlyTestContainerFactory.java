/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.test.grizzly;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import org.glassfish.grizzly.http.server.HttpServer;

public class GrizzlyTestContainerFactory implements TestContainerFactory {

    private static class GrizzlyTestContainer implements TestContainer {

        private final URI uri;
        private final ApplicationHandler appHandler;
        private HttpServer server;
        private static final Logger LOGGER = Logger.getLogger(GrizzlyTestContainer.class.getName());

        private GrizzlyTestContainer(URI uri, ApplicationHandler appHandler) {
            this.appHandler = appHandler;
            this.uri = uri;
        }

        @Override
        public ClientConfig getClientConfig() {
            return null;
        }

        @Override
        public URI getBaseUri() {
            return uri;
        }

        @Override
        public void start() {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Starting GrizzlyTestContainer...");
            }

            try {
                this.server = GrizzlyHttpServerFactory.createHttpServer(uri, appHandler);
            } catch (ProcessingException e) {
                throw new TestContainerException(e);
            }
        }

        @Override
        public void stop() {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Stopping GrizzlyTestContainer...");
            }
            this.server.stop();
        }
    }

    @Override
    public TestContainer create(URI uri, ApplicationHandler appHandler) throws IllegalArgumentException {
        return new GrizzlyTestContainer(uri, appHandler);
    }
}
