package com.itram.bluetoothcore.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class BleScanRepositoryImpl_Factory implements Factory<BleScanRepositoryImpl> {
  @Override
  public BleScanRepositoryImpl get() {
    return newInstance();
  }

  public static BleScanRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BleScanRepositoryImpl newInstance() {
    return new BleScanRepositoryImpl();
  }

  private static final class InstanceHolder {
    static final BleScanRepositoryImpl_Factory INSTANCE = new BleScanRepositoryImpl_Factory();
  }
}
