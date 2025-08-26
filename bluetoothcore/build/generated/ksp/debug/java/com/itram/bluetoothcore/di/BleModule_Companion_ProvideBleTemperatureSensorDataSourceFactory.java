package com.itram.bluetoothcore.di;

import android.content.Context;
import com.itram.bluetoothcore.data.BleTemperatureSensorDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ActivityContext")
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
public final class BleModule_Companion_ProvideBleTemperatureSensorDataSourceFactory implements Factory<BleTemperatureSensorDataSource> {
  private final Provider<Context> contextProvider;

  private BleModule_Companion_ProvideBleTemperatureSensorDataSourceFactory(
      Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public BleTemperatureSensorDataSource get() {
    return provideBleTemperatureSensorDataSource(contextProvider.get());
  }

  public static BleModule_Companion_ProvideBleTemperatureSensorDataSourceFactory create(
      Provider<Context> contextProvider) {
    return new BleModule_Companion_ProvideBleTemperatureSensorDataSourceFactory(contextProvider);
  }

  public static BleTemperatureSensorDataSource provideBleTemperatureSensorDataSource(
      Context context) {
    return Preconditions.checkNotNullFromProvides(BleModule.Companion.provideBleTemperatureSensorDataSource(context));
  }
}
