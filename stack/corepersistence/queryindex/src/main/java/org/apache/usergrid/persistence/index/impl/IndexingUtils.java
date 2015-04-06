package org.apache.usergrid.persistence.index.impl;/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.util.UUID;

import org.apache.usergrid.persistence.core.scope.ApplicationScope;
import org.apache.usergrid.persistence.index.CandidateResult;
import org.apache.usergrid.persistence.index.IndexEdge;
import org.apache.usergrid.persistence.index.SearchEdge;
import org.apache.usergrid.persistence.model.entity.Entity;
import org.apache.usergrid.persistence.model.entity.Id;
import org.apache.usergrid.persistence.model.entity.SimpleId;


public class IndexingUtils {


    // These are not allowed in document type names: _ . , | #
    public static final String FIELD_SEPERATOR = "__";

    public static final String ID_SEPERATOR = "_";


    /**
     * Entity type in ES we put everything into
     */
    public static final String ES_ENTITY_TYPE = "entity";

    /**
     * Reserved UG fields in the document
     */
    public static final String APPLICATION_ID_FIELDNAME = "applicationId";

    public static final String ENTITY_ID_FIELDNAME = "entityId";

    public static final String ENTITY_VERSION_FIELDNAME = "entityVersion";

    public static final String ENTITY_TYPE_FIELDNAME = "entityType";

    public static final String EDGE_NODE_ID_FIELDNAME = "edgeNodeId";

    public static final String EDGE_NAME_FIELDNAME = "edgeName";

    public static final String EDGE_NODE_TYPE_FIELDNAME = "edgeType";

    public static final String EDGE_TIMESTAMP_FIELDNAME = "edgeTimestamp";

    public static final String EDGE_SEARCH_FIELDNAME = "edgeSearch";

    public static final String ENTITY_FIELDS = "fields";


    /**
     * Reserved field types in our document
     */
    public static final String FIELD_NAME = "name";
    public static final String FIELD_BOOLEAN = "boolean";
    public static final String FIELD_INT = "int";
    public static final String FIELD_LONG = "long";
    public static final String FIELD_FLOAT = "float";
    public static final String FIELD_DOUBLE = "double";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_STRING = "string";


    /**
     * All possible sort values
     */
    public static final String SORT_FIELD_BOOLEAN = ENTITY_FIELDS + ".boolean";
    public static final String SORT_FIELD_INT = ENTITY_FIELDS + ".int";
    public static final String SORT_FIELD_LONG = ENTITY_FIELDS + ".long";
    public static final String SORT_FIELD_FLOAT = ENTITY_FIELDS + ".float";
    public static final String SORT_FIELD_DOUBLE = ENTITY_FIELDS + ".double";
    public static final String SORT_FIELD_LOCATION = ENTITY_FIELDS + ".location";
    public static final String SORT_FIELD_STRING = ENTITY_FIELDS + ".string";


    //The value appended to the string field when it's an exact match.  Should only be used on search, never index
    public static final String FIELD_STRING_EQUALS = FIELD_STRING + ".exact";


    /**
     * Create our sub scope.  This is the ownerUUID + type
     */
    public static String createContextName( final ApplicationScope applicationScope, final SearchEdge scope ) {
        StringBuilder sb = new StringBuilder();
        idString( sb, applicationScope.getApplication() );
        sb.append( FIELD_SEPERATOR );
        idString( sb, scope.getNodeId() );
        sb.append( FIELD_SEPERATOR );
        sb.append( scope.getEdgeName() );
        sb.append( FIELD_SEPERATOR );
        sb.append( scope.getNodeType() );
        return sb.toString();
    }


    /**
     * Append the id to the string
     */
    public static final void idString( final StringBuilder builder, final Id id ) {
        builder.append( id.getUuid() ).append( ID_SEPERATOR ).append( id.getType().toLowerCase() );
    }


    /**
     * Turn the id into a string
     */
    public static final String idString( final Id id ) {
        final StringBuilder sb = new StringBuilder();
        idString( sb, id );
        return sb.toString();
    }


    /**
     * Create the index doc from the given entity
     */
    public static String createIndexDocId( final ApplicationScope applicationScope, final Entity entity,
                                           final IndexEdge indexEdge ) {
        return createIndexDocId( applicationScope, entity.getId(), entity.getVersion(), indexEdge );
    }


    /**
     * Create the doc Id. This is the entitie's type + uuid + version
     */
    public static String createIndexDocId( final ApplicationScope applicationScope, final Id entityId,
                                           final UUID version, final SearchEdge searchEdge ) {

        StringBuilder sb = new StringBuilder();
        idString( applicationScope.getApplication() );
        sb.append( FIELD_SEPERATOR );
        idString( sb, entityId );
        sb.append( FIELD_SEPERATOR );
        sb.append( version.toString() );

        sb.append( FIELD_SEPERATOR );

        idString( searchEdge.getNodeId() );

        sb.append( FIELD_SEPERATOR );
        sb.append( searchEdge.getEdgeName() );
        sb.append( FIELD_SEPERATOR );
        sb.append( searchEdge.getNodeType() );

        return sb.toString();
    }


    /**
     * Parse the document id into a candidate result
     */
    public static CandidateResult parseIndexDocId( final String documentId ) {

        String[] idparts = documentId.split( FIELD_SEPERATOR );
        String entityIdString = idparts[1];
        String version = idparts[2];

        final String[] entityIdParts = entityIdString.split( ID_SEPERATOR );

        Id entityId = new SimpleId( UUID.fromString( entityIdParts[0] ), entityIdParts[1] );

        return new CandidateResult( entityId, UUID.fromString( version ) );
    }


    public static String getType( ApplicationScope applicationScope, Id entityId ) {
        return getType( applicationScope, entityId.getType() );
    }


    public static String getType( ApplicationScope applicationScope, String type ) {
        return idString( applicationScope.getApplication() ) + FIELD_SEPERATOR + type;
    }
}
