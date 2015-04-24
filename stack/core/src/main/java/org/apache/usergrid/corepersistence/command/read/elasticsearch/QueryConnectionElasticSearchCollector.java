/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.usergrid.corepersistence.command.read.elasticsearch;


import org.apache.usergrid.corepersistence.ManagerCache;
import org.apache.usergrid.corepersistence.command.read.elasticsearch.impl.ConnectionResultsLoaderFactoryImpl;
import org.apache.usergrid.corepersistence.command.read.elasticsearch.impl.ResultsLoaderFactory;
import org.apache.usergrid.persistence.EntityRef;
import org.apache.usergrid.persistence.Query;
import org.apache.usergrid.persistence.core.scope.ApplicationScope;
import org.apache.usergrid.persistence.index.ApplicationEntityIndex;
import org.apache.usergrid.persistence.index.SearchEdge;
import org.apache.usergrid.persistence.index.SearchTypes;

import com.google.inject.Inject;


/**
 * Command for querying connections
 */
public class QueryConnectionElasticSearchCollector extends AbstractQueryElasticSearchCollector {

    private final ManagerCache managerCache;
    private final EntityRef headEntity;
    private final String connectionName;

    @Inject
    protected QueryConnectionElasticSearchCollector( final ApplicationEntityIndex entityIndex,
                                                     final ApplicationScope applicationScope,
                                                     final SearchEdge indexScope, final SearchTypes searchTypes,
                                                     final Query query, final ManagerCache managerCache,
                                                     final EntityRef headEntity, final String connectionName ) {
        super( entityIndex, applicationScope, indexScope, searchTypes, query );
        this.managerCache = managerCache;
        this.headEntity = headEntity;
        this.connectionName = connectionName;
    }


    @Override
    protected ResultsLoaderFactory getResultsLoaderFactory() {
        return new ConnectionResultsLoaderFactoryImpl( managerCache, headEntity, connectionName );
    }
}
