/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * @param <ENTITY> エンティティ
 */
public abstract class AutoBatchModifyQuery<ENTITY> extends AbstractQuery
    implements BatchModifyQuery {

  protected static final String[] EMPTY_STRINGS = new String[] {};

  protected List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes;

  protected List<EntityPropertyType<ENTITY, ?>> idPropertyTypes;

  protected String[] includedPropertyNames = EMPTY_STRINGS;

  protected String[] excludedPropertyNames = EMPTY_STRINGS;

  protected final EntityType<ENTITY> entityType;

  protected VersionPropertyType<? super ENTITY, ENTITY, ?, ?> versionPropertyType;

  protected TenantIdPropertyType<? super ENTITY, ENTITY, ?, ?> tenantIdPropertyType;

  protected boolean optimisticLockCheckRequired;

  protected boolean autoGeneratedKeysSupported;

  protected boolean executable;

  protected SqlExecutionSkipCause executionSkipCause =
      SqlExecutionSkipCause.BATCH_TARGET_NONEXISTENT;

  protected List<PreparedSql> sqls;

  protected List<ENTITY> entities;

  protected ENTITY currentEntity;

  protected int batchSize;

  protected SqlLogType sqlLogType;

  public AutoBatchModifyQuery(EntityType<ENTITY> entityType) {
    assertNotNull(entityType);
    this.entityType = entityType;
  }

  protected void prepareIdAndVersionPropertyTypes() {
    idPropertyTypes = entityType.getIdPropertyTypes();
    versionPropertyType = entityType.getVersionPropertyType();
    tenantIdPropertyType = entityType.getTenantIdPropertyType();
  }

  protected void validateIdExistent() {
    if (idPropertyTypes.isEmpty()) {
      throw new JdbcException(Message.DOMA2022, entityType.getName());
    }
  }

  protected void prepareOptions() {
    if (queryTimeout <= 0) {
      queryTimeout = config.getQueryTimeout();
    }
    if (batchSize <= 0) {
      batchSize = config.getBatchSize();
    }
  }

  protected boolean isTargetPropertyName(String name) {
    if (includedPropertyNames.length > 0) {
      for (String includedName : includedPropertyNames) {
        if (includedName.equals(name)) {
          for (String excludedName : excludedPropertyNames) {
            if (excludedName.equals(name)) {
              return false;
            }
          }
          return true;
        }
      }
      return false;
    }
    if (excludedPropertyNames.length > 0) {
      for (String excludedName : excludedPropertyNames) {
        if (excludedName.equals(name)) {
          return false;
        }
      }
      return true;
    }
    return true;
  }

  public void setEntities(Iterable<ENTITY> entities) {
    assertNotNull(entities);
    if (entities instanceof Collection<?>) {
      this.entities = new ArrayList<ENTITY>((Collection<ENTITY>) entities);
    } else {
      this.entities = new ArrayList<ENTITY>();
      for (ENTITY entity : entities) {
        this.entities.add(entity);
      }
    }
    this.sqls = new ArrayList<PreparedSql>(this.entities.size());
  }

  public List<ENTITY> getEntities() {
    return entities;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public void setIncludedPropertyNames(String... includedPropertyNames) {
    this.includedPropertyNames = includedPropertyNames;
  }

  public void setExcludedPropertyNames(String... excludedPropertyNames) {
    this.excludedPropertyNames = excludedPropertyNames;
  }

  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  @Override
  public PreparedSql getSql() {
    return sqls.get(0);
  }

  @Override
  public List<PreparedSql> getSqls() {
    return sqls;
  }

  @Override
  public boolean isOptimisticLockCheckRequired() {
    return optimisticLockCheckRequired;
  }

  @Override
  public boolean isAutoGeneratedKeysSupported() {
    return autoGeneratedKeysSupported;
  }

  @Override
  public boolean isExecutable() {
    return executable;
  }

  @Override
  public SqlExecutionSkipCause getSqlExecutionSkipCause() {
    return executionSkipCause;
  }

  @Override
  public int getBatchSize() {
    return batchSize;
  }

  @Override
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  @Override
  public String toString() {
    return sqls.toString();
  }
}
