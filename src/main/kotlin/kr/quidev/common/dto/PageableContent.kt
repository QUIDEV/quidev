package kr.quidev.common.dto

import org.springframework.data.domain.Page

data class PageableContent(
    val content: List<Any>,
    val pageNo: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int
) {
    companion object {
        fun of(page: Page<out Any>): PageableContent {
            return PageableContent(
                page.content,
                page.pageable.pageNumber,
                page.pageable.pageSize,
                page.totalElements,
                page.totalPages
            )
        }
    }
}
