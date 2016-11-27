/*
 * -----------------------------------------------------------------------\
 * Lumeer
 *  
 * Copyright (C) 2016 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package io.lumeer.engine.api.data;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a data storage.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 * @author <a href="mailto:kubedo8@gmail.com">Jakub Rodák</a>
 * @author <a href="mailto:mat.per.vt@gmail.com">Matej Perejda</a>
 */
public interface DataStorage {

   void connect(final List<StorageConnection> connections, final String database);

   default void connect(final StorageConnection connection, final String database) {
      connect(Collections.singletonList(connection), database);
   }

   void disconnect();

   /**
    * Returns a List object of all collection names in the database.
    *
    * @return the list of all collection names
    */
   List<String> getAllCollections();

   /**
    * Creates a new collection with the specified name.
    *
    * @param collectionName
    *       the name of the collection to create
    */
   void createCollection(final String collectionName);

   /**
    * Drops the collection with the specified name.
    *
    * @param collectionName
    *       the name of the collection to drop
    */
   void dropCollection(final String collectionName);

   /**
    * Creates and inserts a new document to specified collection.
    *
    * @param collectionName
    *       the name of the collection where the document will be created
    * @param document
    *       the DataDocument object representing a document to be created
    * @return the id of the newly created document
    */
   String createDocument(final String collectionName, final DataDocument document);

   /**
    * Creates and inserts an old document to specified collection.
    *
    * @param collectionName
    *       the name of the collection where the document will be created
    * @param document
    *       the DataDocument object representing a document to be created
    * @param documentId
    *       the id of the document
    * @param version
    *       the version of document
    */
   void createOldDocument(final String collectionName, final DataDocument document, String documentId, int version) throws Exception;

   /**
    * Reads the specified document in given collection by its id.
    *
    * @param collectionName
    *       the name of the collection where the document is located
    * @param documentId
    *       the id of the read document
    * @return the DataDocument object representing the read document
    */
   DataDocument readDocument(final String collectionName, final String documentId);

   /**
    * Reads the old document in given collection by its id and version.
    *
    * @param collectionName
    *       the name of the collection where the document is located
    * @param documentId
    *       the id of the document
    * @param version
    *       the version of document
    * @return the DataDocument object representing the read document
    */
   DataDocument readOldDocument(final String collectionName, final String documentId, final int version);

   /**
    * Modifies an existing document in given collection by its id. If updated document contains non-existing columns, they will be added into database.
    *
    * @param collectionName
    *       the name of the collection where the existing document is located
    * @param updatedDocument
    *       the DataDocument object representing a document with changes to update
    * @param documentId
    *       the id of the existing document in given collection
    * @param targetVersion
    *       The version of document to update. Use -1 for disable filter for version attribute.
    */
   void updateDocument(final String collectionName, final DataDocument updatedDocument, final String documentId, int targetVersion);

   /**
    * Drops an existing document in given collection by its id.
    *
    * @param collectionName
    *       the name of the collection where the document is located
    * @param documentId
    *       the id of the document to drop
    */
   void dropDocument(final String collectionName, final String documentId);

   /**
    * Reads the old document in given collection by its id and version.
    *
    * @param collectionName
    *       the name of the collection where the document is located
    * @param documentId
    *       the id of the document
    * @param version
    *       the version of document
    */
   void dropOldDocument(final String collectionName, final String documentId, final int version);

   /**
    * Drops many documents based on filter
    *
    * @param collectionName
    *       the name of the collection
    * @param filter
    *       string representation of filter
    */
   void dropManyDocuments(final String collectionName, final String filter);

   /**
    * Updates the name of an attribute which is found in all documents of given collection.
    *
    * @param collectionName
    *       the name of the collection where the given attribute should be renamed
    * @param oldName
    *       the old name of an attribute
    * @param newName
    *       the new name of an attribute
    */
   void renameAttribute(final String collectionName, final String oldName, final String newName);

   /**
    * Removes given attribute from existing document specified by its id.
    *
    * @param collectionName
    *       the name of the collection where the given attribute should be removed
    * @param documentId
    *       the id of document from which attribute will be removed
    * @param attributeName
    *       the name of an attribute to remove
    */
   void removeAttribute(final String collectionName, final String documentId, final String attributeName);

   /**
    * Gets the first 100 distinct values of the given attribute in the given collection.
    *
    * @param collectionName
    *       the name of the collection where documents contain the given attribute
    * @param attributeName
    *       the name of the attribute
    * @return the distinct set of values of the given attribute
    */
   Set<String> getAttributeValues(final String collectionName, final String attributeName);

   /**
    * Executes a query to find and return documents.
    *
    * @param query
    *       the database find command specified as a JSON string
    * @return the list of the found documents
    * @see <a href="https://docs.mongodb.com/v3.2/reference/command/find/#dbcmd.find">https://docs.mongodb.com/v3.2/reference/command/find/#dbcmd.find</a>
    */
   List<DataDocument> search(final String query);

   /**
    * Searches the specified collection for specified documents using filter, sort, skip and limit option.
    *
    * @param collectionName
    *       the name of the collection where the search will be performed
    * @param filter
    *       the query predicate. If unspecified, then all documents in the collection will match the predicate.
    * @param sort
    *       the sort specification for the ordering of the results. If unspecified, then a sort is equivalent to setting no sort.
    * @param skip
    *       the number of documents to skip. A skip of 0 is equivalent to setting no skip.
    * @param limit
    *       the maximum number of documents to return. A limit of 0 is equivalent to setting no limit.
    * @return the list of the found documents
    */
   List<DataDocument> search(final String collectionName, final String filter, final String sort, final int skip, final int limit);

   /**
    * Increment attribute value of document by specified amount. If the field does not exist, it creates the field and sets the field to the specified value.
    *
    * @param collectionName
    *       the name of the collection where the given document is located
    * @param documentId
    *       the id of specified document
    * @param attributeName
    *       the name of attribute which value is increment
    * @param incBy
    *       the value by which attribute is increment
    */
   void incerementAttributeValueBy(final String collectionName, final String documentId, final String attributeName, final int incBy);

}
