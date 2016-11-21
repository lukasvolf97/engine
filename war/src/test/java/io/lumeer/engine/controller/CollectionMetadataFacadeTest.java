/*
 * -----------------------------------------------------------------------\
 * Lumeer
 *  
 * Copyright (C) 2016 - 2017 the original author or authors.
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
package io.lumeer.engine.controller;

import io.lumeer.engine.api.data.DataStorage;
import io.lumeer.engine.exception.CollectionAlreadyExistsException;
import io.lumeer.engine.exception.CollectionNotFoundException;
import io.lumeer.engine.util.Utils;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
import javax.inject.Inject;

/**
 * @author <a href="alica.kacengova@gmail.com">Alica Kačengová</a>
 */
public class CollectionMetadataFacadeTest extends Arquillian {

   @Deployment
   public static Archive<?> createTestArchive() {
      return ShrinkWrap.create(WebArchive.class, "CollectionMetadataFacadeTest.war")
                       .addPackages(true, "io.lumeer", "org.bson", "com.mongodb", "io.netty")
                       .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Inject
   private CollectionMetadataFacade collectionMetadataFacade;

   @Inject
   private CollectionFacade collectionFacade;

   @Inject
   private DataStorage dataStorage;

   private final String TEST_COLLECTION_INTERNAL_NAME = "collection.collection1";
   private final String TEST_COLLECTION_REAL_NAME = "Collection_1";
   private final String TEST_COLLECTION_METADATA_COLLECTION_NAME = "meta.collection.collection1";

   @Test
   public void testCollectionNameToInternalForm() throws Exception {
      String originalName = "čťH-%/e&äll o1";
      String newName = "collection.hello1";
      Assert.assertEquals(collectionMetadataFacade.collectionNameToInternalForm(originalName), newName);
   }

   @Test
   public void testGetCollectionAttributesInfo() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);
      String name = "column 1";
      String type = "int";
      collectionMetadataFacade.addCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, name, type, -1);

      Map<String, String> columnsInfo = collectionMetadataFacade.getCollectionAttributesInfo(TEST_COLLECTION_INTERNAL_NAME);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);

      Assert.assertEquals(columnsInfo.size(), 1);
      Assert.assertTrue(columnsInfo.containsKey(name));
      Assert.assertTrue(columnsInfo.containsValue(type));
   }

   @Test
   public void testAddCollectionAttributeNew() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);
      boolean add = collectionMetadataFacade.addCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1", "int", -1);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);

      Assert.assertTrue(add);
   }

   @Test
   public void testAddCollectionAttributeExisting() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);
      collectionMetadataFacade.addCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1", "int", -1);
      boolean add = collectionMetadataFacade.addCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1", "int", -1);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);

      Assert.assertFalse(add);
   }

   @Test
   public void testRenameCollectionAttribute() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);
      collectionMetadataFacade.addCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1", "int", -1);
      boolean rename = collectionMetadataFacade.renameCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1", "column 2");
      Map<String, String> columnsInfo = collectionMetadataFacade.getCollectionAttributesInfo(TEST_COLLECTION_INTERNAL_NAME);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);

      Assert.assertTrue(columnsInfo.containsKey("column 2"));
      Assert.assertTrue(rename);
   }

   @Test
   public void testRetypeCollectionAttribute() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);
      collectionMetadataFacade.addCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1", "int", -1);
      boolean retype = collectionMetadataFacade.retypeCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1", "double");
      Map<String, String> columnsInfo = collectionMetadataFacade.getCollectionAttributesInfo(TEST_COLLECTION_INTERNAL_NAME);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);

      Assert.assertTrue(columnsInfo.containsValue("double"));
      Assert.assertTrue(retype);
   }

   @Test
   public void testDropCollectionAttribute() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);
      collectionMetadataFacade.addCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1", "int", -1);
      boolean drop = collectionMetadataFacade.dropCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, "column 1");
      Map<String, String> columnsInfo = collectionMetadataFacade.getCollectionAttributesInfo(TEST_COLLECTION_INTERNAL_NAME);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);

      Assert.assertTrue(columnsInfo.isEmpty());
      Assert.assertTrue(drop);
   }

   @Test
   public void testSetGetOriginalCollectionName() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME); // set is done in this method
      String realName = collectionMetadataFacade.getOriginalCollectionName(TEST_COLLECTION_INTERNAL_NAME);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);
      Assert.assertEquals(TEST_COLLECTION_REAL_NAME, realName);
   }

   @Test
   public void testCollectionMetadataCollectionName() {
      Assert.assertEquals(collectionMetadataFacade.collectionMetadataCollectionName(TEST_COLLECTION_INTERNAL_NAME), TEST_COLLECTION_METADATA_COLLECTION_NAME);
   }

   @Test
   public void testCreateInitialMetadata() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      dataStorage.createCollection(collectionMetadataFacade.collectionMetadataCollectionName(TEST_COLLECTION_INTERNAL_NAME));
      collectionMetadataFacade.createInitialMetadata(TEST_COLLECTION_REAL_NAME);

      String name = collectionMetadataFacade.getOriginalCollectionName(TEST_COLLECTION_INTERNAL_NAME);
      String lock = collectionMetadataFacade.getCollectionLockTime(TEST_COLLECTION_INTERNAL_NAME);
      long count = collectionMetadataFacade.getCollectionCount(TEST_COLLECTION_INTERNAL_NAME); // TODO

      dataStorage.dropCollection(collectionMetadataFacade.collectionMetadataCollectionName(TEST_COLLECTION_INTERNAL_NAME));

      Assert.assertEquals(name, TEST_COLLECTION_REAL_NAME);
      Assert.assertNotEquals(lock, "");
      Assert.assertEquals(count, 0);
   }

   @Test
   public void testSetGetCollectionLockTime() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);
      String time = Utils.getCurrentTimeString();
      collectionMetadataFacade.setCollectionLockTime(TEST_COLLECTION_INTERNAL_NAME, time);
      String timeTest = collectionMetadataFacade.getCollectionLockTime(TEST_COLLECTION_INTERNAL_NAME);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);
      Assert.assertEquals(time, timeTest);
   }

   @Test
   public void testGetCollectionCount() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);
      long count = collectionMetadataFacade.getCollectionCount(TEST_COLLECTION_INTERNAL_NAME);
      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);
      Assert.assertEquals(count, 0);
   }

   @Test
   public void testIncrementDecrementCollectionCount() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);

      collectionMetadataFacade.incrementCollectionCount(TEST_COLLECTION_INTERNAL_NAME);
      long count1 = collectionMetadataFacade.getCollectionCount(TEST_COLLECTION_INTERNAL_NAME);

      collectionMetadataFacade.decrementCollectionCount(TEST_COLLECTION_INTERNAL_NAME);
      long count2 = collectionMetadataFacade.getCollectionCount(TEST_COLLECTION_INTERNAL_NAME);

      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);
      Assert.assertEquals(count1, 1);
      Assert.assertEquals(count2, 0);
   }

   @Test
   public void testIncrementDecrementAttributeCount() throws CollectionAlreadyExistsException, CollectionNotFoundException {
      collectionFacade.createCollection(TEST_COLLECTION_REAL_NAME);

      String attribute = "a1";
      collectionMetadataFacade.addCollectionAttribute(TEST_COLLECTION_INTERNAL_NAME, attribute, "", -1);

      collectionMetadataFacade.incrementAttributeCount(TEST_COLLECTION_INTERNAL_NAME, attribute);
      long count1 = collectionMetadataFacade.getAttributeCount(TEST_COLLECTION_INTERNAL_NAME, attribute);

      collectionMetadataFacade.decrementAttributeCount(TEST_COLLECTION_INTERNAL_NAME, attribute);
      long count2 = collectionMetadataFacade.getAttributeCount(TEST_COLLECTION_INTERNAL_NAME, attribute);

      collectionFacade.dropCollection(TEST_COLLECTION_INTERNAL_NAME);

      Assert.assertEquals(count1, 1);
      Assert.assertEquals(count2, 0);
   }

   @Test
   public void testAddDocumentAttributes() {
      // TODO
   }

   @Test
   public void testDropDocumentAttributes() {
      // TODO
   }

   @Test
   public void testIsUserCollectionYes() {
      Assert.assertTrue(collectionMetadataFacade.isUserCollection(TEST_COLLECTION_INTERNAL_NAME));
   }

   @Test
   public void testIsUserCollectionNo() {
      Assert.assertFalse(collectionMetadataFacade.isUserCollection(TEST_COLLECTION_METADATA_COLLECTION_NAME));
   }

}