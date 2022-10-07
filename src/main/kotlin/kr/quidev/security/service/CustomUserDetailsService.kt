package kr.quidev.security.service

import kr.quidev.member.repository.MemberRepository
import kr.quidev.security.domain.MemberContext
import org.slf4j.LoggerFactory.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("UserDetailsService")
class CustomUserDetailsService(val memberRepository: MemberRepository) : UserDetailsService {

    val log = getLogger(this.javaClass)

    override fun loadUserByUsername(username: String): UserDetails {

        val member = memberRepository.findMemberByEmail(username).orElse(null)
            ?: throw UsernameNotFoundException("invalid email address")

        val roles = mutableListOf<GrantedAuthority>()
        roles.add(SimpleGrantedAuthority(member.role))

        return MemberContext(member, roles)

    }

}
