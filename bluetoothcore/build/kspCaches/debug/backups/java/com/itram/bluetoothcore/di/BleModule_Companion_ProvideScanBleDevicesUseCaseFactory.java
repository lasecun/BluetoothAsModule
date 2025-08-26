package com.itram.bluetoothcore.di;

import com.itram.bluetoothcore.domain.repository.BleScanRepository;
import com.itram.bluetoothcore.domain.usecase.ScanBleDevicesUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class BleModule_Companion_ProvideScanBleDevicesUseCaseFactory implements Factory<ScanBleDevicesUseCase> {
  private final Provider<BleScanRepository> repositoryProvider;

  private BleModule_Companion_ProvideScanBleDevicesUseCaseFactory(
      Provider<BleScanRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ScanBleDevicesUseCase get() {
    return provideScanBleDevicesUseCase(repositoryProvider.get());
  }

  public static BleModule_Companion_ProvideScanBleDevicesUseCaseFactory create(
      Provider<BleScanRepository> repositoryProvider) {
    return new BleModule_Companion_ProvideScanBleDevicesUseCaseFactory(repositoryProvider);
  }

  public static ScanBleDevicesUseCase provideScanBleDevicesUseCase(BleScanRepository repository) {
    return Preconditions.checkNotNullFromProvides(BleModule.Companion.provideScanBleDevicesUseCase(repository));
  }
}
