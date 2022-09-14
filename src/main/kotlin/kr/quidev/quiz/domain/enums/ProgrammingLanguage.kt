package kr.quidev.quiz.domain.enums

enum class ProgrammingLanguage(private val value: String) {

    JAVA("Java"), KOTLIN("Kotlin"), PYTHON("Python"), C("C"), RUBY("Ruby"), RUST("Rush"), GO("Go"), JAVASCRIPT("Javascript");

    fun getValue(): String {
        return value
    }

}
