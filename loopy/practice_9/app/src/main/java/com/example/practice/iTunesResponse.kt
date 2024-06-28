package com.example.practice

import com.google.gson.annotations.SerializedName

// iTunes API의 JSON 응답을 나타내는 데이터 클래스
data class iTunesResponse(
    // 응답 결과의 총 개수
    @SerializedName("resultCount") val resultCount: Int,
    // 트랙의 리스트
    @SerializedName("results") val results: List<Track>
)

// 트랙 정보를 나타내는 데이터 클래스
data class Track(
    // JSON 필드를 Kotlin 필드에 매핑하는 어노테이션
    @SerializedName("wrapperType") val wrapperType: String, // 트랙 유형 (예: track)
    @SerializedName("kind") val kind: String, // 콘텐츠 유형 (예: song)
    @SerializedName("artistId") val artistId: Int, // 아티스트 ID
    @SerializedName("collectionId") val collectionId: Int, // 앨범 ID
    @SerializedName("trackId") val trackId: Int, // 트랙 ID
    @SerializedName("artistName") val artistName: String, // 아티스트 이름
    @SerializedName("collectionName") val collectionName: String, // 앨범 이름
    @SerializedName("trackName") val trackName: String, // 트랙 이름
    @SerializedName("collectionCensoredName") val collectionCensoredName: String, // 검열된 앨범 이름
    @SerializedName("trackCensoredName") val trackCensoredName: String, // 검열된 트랙 이름
    @SerializedName("artistViewUrl") val artistViewUrl: String, // 아티스트 보기 URL
    @SerializedName("collectionViewUrl") val collectionViewUrl: String, // 앨범 보기 URL
    @SerializedName("trackViewUrl") val trackViewUrl: String, // 트랙 보기 URL
    @SerializedName("previewUrl") val previewUrl: String, // 미리 듣기 URL
    @SerializedName("artworkUrl30") val artworkUrl30: String, // 30x30 크기의 앨범 표지 URL
    @SerializedName("artworkUrl60") val artworkUrl60: String, // 60x60 크기의 앨범 표지 URL
    @SerializedName("artworkUrl100") val artworkUrl100: String, // 100x100 크기의 앨범 표지 URL
    @SerializedName("collectionPrice") val collectionPrice: Double, // 앨범 가격
    @SerializedName("trackPrice") val trackPrice: Double, // 트랙 가격
    @SerializedName("releaseDate") val releaseDate: String, // 출시 날짜
    @SerializedName("collectionExplicitness") val collectionExplicitness: String, // 앨범의 명확성 (explicit 또는 notExplicit)
    @SerializedName("trackExplicitness") val trackExplicitness: String, // 트랙의 명확성 (explicit 또는 notExplicit)
    @SerializedName("discCount") val discCount: Int, // 디스크 수
    @SerializedName("discNumber") val discNumber: Int, // 디스크 번호
    @SerializedName("trackCount") val trackCount: Int, // 트랙 수
    @SerializedName("trackNumber") val trackNumber: Int, // 트랙 번호
    @SerializedName("trackTimeMillis") val trackTimeMillis: Int, // 트랙 길이 (밀리초 단위)
    @SerializedName("country") val country: String, // 국가
    @SerializedName("currency") val currency: String, // 통화 (예: USD)
    @SerializedName("primaryGenreName") val primaryGenreName: String, // 주 장르 이름
    @SerializedName("isStreamable") val isStreamable: Boolean // 스트리밍 가능 여부
)
