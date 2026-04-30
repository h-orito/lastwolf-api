package com.ort.lastwolf.fw.security

import com.ort.dbflute.allcommon.CDef
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class LastwolfUser(
    val uid: String,
    val authority: CDef.Authority,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(this.authority.code()))

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = this.uid

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = ""

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}
