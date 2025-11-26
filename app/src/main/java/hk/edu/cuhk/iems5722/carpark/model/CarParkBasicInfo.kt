package hk.edu.cuhk.iems5722.carpark.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarParkBasicInfo(
    @SerialName("park_id")
    val parkId: String,

    @SerialName("name_en")
    val nameEn: String,

    @SerialName("name_tc")
    val nameTc: String? = null,

    @SerialName("name_sc")
    val nameSc: String? = null,

    @SerialName("displayAddress_en")
    val displayAddressEn: String,

    @SerialName("displayAddress_tc")
    val displayAddressTc: String? = null,

    @SerialName("displayAddress_sc")
    val displayAddressSc: String? = null,

    @SerialName("latitude")
    val latitude: Double? = null,

    @SerialName("longitude")
    val longitude: Double? = null,

    @SerialName("district_en")
    val districtEn: String? = null,

    @SerialName("district_tc")
    val districtTc: String? = null,

    @SerialName("district_sc")
    val districtSc: String? = null,

    @SerialName("contactNo")
    val contactNo: String? = null,

    @SerialName("opening_status")
    val openingStatus: String? = null,

    @SerialName("height")
    val height: Double? = null,

    @SerialName("remark_en")
    val remarkEn: String? = null,

    @SerialName("remark_tc")
    val remarkTc: String? = null,

    @SerialName("remark_sc")
    val remarkSc: String? = null,

    @SerialName("website_en")
    val websiteEn: String? = null,

    @SerialName("website_tc")
    val websiteTc: String? = null,

    @SerialName("website_sc")
    val websiteSc: String? = null,

    @SerialName("carpark_photo")
    val carparkPhoto: String,
)

val CarParkBasicInfo.carparkPhotoHttps: String
    get() = if (carparkPhoto.startsWith("http://", ignoreCase = true)) {
        "https://" + carparkPhoto.removePrefix("http://")
    } else {
        carparkPhoto
    }

@Serializable
data class CarParkBasicInfoResponse(
    @SerialName("car_park")
    val carParks: List<CarParkBasicInfo>
)
