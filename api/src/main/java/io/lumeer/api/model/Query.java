/*
 * Lumeer: Modern Data Definition and Processing Platform
 *
 * Copyright (C) since 2017 Answer Institute, s.r.o. and/or its affiliates.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.lumeer.api.model;

import java.util.Set;

public interface Query {

   Set<String> getFilters();

   Set<String> getCollectionIds();

   Set<String> getDocumentIds();

   Set<String> getLinkTypeIds();

   String getFulltext();

   Integer getPage();

   Integer getPageSize();

   default boolean isMoreSpecificThan(Query otherQuery) {
      if (otherQuery.getCollectionIds() != null && otherQuery.getCollectionIds().size() > 0) {
         if (getCollectionIds() == null || !getCollectionIds().containsAll(otherQuery.getCollectionIds())) {
            return false;
         }
      }

      if (otherQuery.getDocumentIds() != null && otherQuery.getDocumentIds().size() > 0) {
         if (getDocumentIds() == null || !getDocumentIds().containsAll(otherQuery.getDocumentIds())) {
            return false;
         }
      }

      if (otherQuery.getLinkTypeIds() != null && otherQuery.getLinkTypeIds().size() > 0) {
         if (getLinkTypeIds() == null || !getLinkTypeIds().containsAll(otherQuery.getLinkTypeIds())) {
            return false;
         }
      }

      if (otherQuery.getFilters() != null && otherQuery.getFilters().size() > 0) {
         if (getFilters() == null || !getFilters().containsAll(otherQuery.getFilters())) {
            return false;
         }
      }

      if (otherQuery.getFulltext() != null) {
         if (getFulltext() == null || !getFulltext().startsWith(otherQuery.getFulltext())) {
            return false;
         }
      }

      return true;
   }

}
