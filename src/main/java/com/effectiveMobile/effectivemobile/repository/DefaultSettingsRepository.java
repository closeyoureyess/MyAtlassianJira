package com.effectiveMobile.effectivemobile.repository;

import com.effectiveMobile.effectivemobile.entities.DefaultSettings;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefaultSettingsRepository extends JpaRepository<DefaultSettings, Integer> {

    Optional<DefaultSettings> findByFieldName(DefaultSettingsFieldNameEnum fieldName);

}
