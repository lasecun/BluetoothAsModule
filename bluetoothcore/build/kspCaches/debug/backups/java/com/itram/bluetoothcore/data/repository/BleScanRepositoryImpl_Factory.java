package com.itram.bluetoothcore.data.repository;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
  private final Provider<Context> contextProvider;

  private BleScanRepositoryImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public BleScanRepositoryImpl get() {
    return newInstance(contextProvider.get());
  }

  public static BleScanRepositoryImpl_Factory create(Provider<Context> contextProvider) {
    return new BleScanRepositoryImpl_Factory(contextProvider);
  }

  public static BleScanRepositoryImpl newInstance(Context context) {
    return new BleScanRepositoryImpl(context);
  }
}
