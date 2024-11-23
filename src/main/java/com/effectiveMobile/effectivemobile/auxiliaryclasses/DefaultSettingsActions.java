package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;

import java.util.Optional;

public interface DefaultSettingsActions {

    <T> Optional<T> getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum fieldName, T fieldValue);

}
