/*
 * Lumeer: Modern Data Definition and Processing Platform
 *
 * Copyright (C) since 2017 Lumeer.io, s.r.o. and/or its affiliates.
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
package io.lumeer.storage.api.filter;

import io.lumeer.api.model.ConditionType;

import java.util.Objects;

public class LinkSearchAttributeFilter extends SearchAttributeFilter {

   private final String linkTypeId;

   public LinkSearchAttributeFilter(final String linkTypeId, final ConditionType conditionType, final String attributeId, final Object value) {
      super(conditionType, attributeId, value);
      this.linkTypeId = linkTypeId;
   }

   public String getLinkTypeId() {
      return linkTypeId;
   }


   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof LinkSearchAttributeFilter)) {
         return false;
      }
      if (!super.equals(o)) {
         return false;
      }
      final LinkSearchAttributeFilter that = (LinkSearchAttributeFilter) o;
      return Objects.equals(getAttributeId(), that.getAttributeId()) &&
            Objects.equals(getConditionType(), that.getConditionType()) &&
            Objects.equals(getValue(), that.getValue()) &&
            Objects.equals(getLinkTypeId(), that.getLinkTypeId());
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), getAttributeId(), getConditionType(), getValue(), getLinkTypeId());
   }

   @Override
   public String toString() {
      return "LinkSearchAttributeFilter{" +
            "linkTypeId='" + getLinkTypeId() + '\'' +
            ", attributeId='" + getAttributeId() + '\'' +
            ", conditionType='" + getConditionType() + '\'' +
            ", value=" + getValue() +
            '}';
   }

}
