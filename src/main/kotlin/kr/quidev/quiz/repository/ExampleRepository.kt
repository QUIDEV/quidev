package kr.quidev.quiz.repository

import kr.quidev.quiz.domain.entity.Example
import org.springframework.data.jpa.repository.JpaRepository

interface ExampleRepository : JpaRepository<Example, Long> {

}
