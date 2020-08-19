package com.ort.lastwolf.application.service

import com.ort.lastwolf.domain.model.version.Version
import com.ort.lastwolf.infrastructure.datasource.version.VersionDataSource
import org.springframework.stereotype.Service

@Service
class VersionService(
    val versionDataSource: VersionDataSource
) {

    fun findVersion(): Version {
        return versionDataSource.findVersion()
    }
}
