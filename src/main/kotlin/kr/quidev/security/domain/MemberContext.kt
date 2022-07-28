package kr.quidev.security.domain

import kr.quidev.member.domain.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class MemberContext(member: Member, authorities: MutableCollection<out GrantedAuthority>?) :
    User(member.email, member.password, authorities) {
}
