package com.bulahej.tazweeg.apis_responses.UserResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName(value="name",  alternate={"id" , "userId"})
    @Expose
    private int id;

    @SerializedName("fullName")
    @Expose
    private String name;

    @SerializedName(value="name1",  alternate={"username" ,"userName" , "code"})
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName(value="name2",  alternate={"mobile", "phone"})
    @Expose
    private String mobile;

    @SerializedName("genderId")
    @Expose
    private int genderId;
    @SerializedName("genderEN")
    @Expose
    private String genderEN;
    @SerializedName("genderAR")
    @Expose
    private String genderAR;

    @SerializedName("typeId")
    @Expose
    private int typeId;

    @SerializedName(value="name7",  alternate={"consultantId" , "conUserId"})
    @Expose
    private int consultantId;

    @SerializedName(value="name3",  alternate={"consulantName" , "consultant", "consultantName"})
    @Expose
    private String consultantName;


    public String getConsultantPhone() {
        return consultantPhone;
    }

    public void setConsultantPhone(String consultantPhone) {
        this.consultantPhone = consultantPhone;
    }

    public Boolean getPolygamy() {
        return isPolygamy;
    }

    public void setPolygamy(Boolean polygamy) {
        isPolygamy = polygamy;
    }

    public Boolean getFamilyShow() {
        return isFamilyShow;
    }

    public void setFamilyShow(Boolean familyShow) {
        isFamilyShow = familyShow;
    }

    public Boolean getViewed() {
        return isViewed;
    }

    public void setViewed(Boolean viewed) {
        isViewed = viewed;
    }

    @SerializedName(value="name6",  alternate={"consultantPhone" , "conPhone" , "cMobile"})
    @Expose
    private String consultantPhone;

    @SerializedName("consulantLastName")
    @Expose
    private String consulantLastName;

    @SerializedName(value="name12",  alternate={"stateId" ,"statesId" })
    @Expose
    private String stateId = "";
    @SerializedName("stateEN")
    @Expose
    private String stateEN;
    @SerializedName("stateAR")
    @Expose
    private String stateAR;

    // Used if user type is consultant
    @SerializedName(value="name4",  alternate={"emirateServeIn" , "consultantStates"})
    @Expose
    private String stateServeIn;

    public String getGenderEN() {
        return genderEN;
    }

    public void setGenderEN(String genderEN) {
        this.genderEN = genderEN;
    }

    public String getGenderAR() {
        return genderAR;
    }

    public void setGenderAR(String genderAR) {
        this.genderAR = genderAR;
    }

    public String getStateEN() {
        return stateEN;
    }

    public void setStateEN(String stateEN) {
        this.stateEN = stateEN;
    }

    public String getStateAR() {
        return stateAR;
    }

    public void setStateAR(String stateAR) {
        this.stateAR = stateAR;
    }

    public String getCountryEN() {
        return countryEN;
    }

    public void setCountryEN(String countryEN) {
        this.countryEN = countryEN;
    }

    public String getCountryAR() {
        return countryAR;
    }

    public void setCountryAR(String countryAR) {
        this.countryAR = countryAR;
    }

    public String getWorkEN() {
        return workEN;
    }

    public void setWorkEN(String workEN) {
        this.workEN = workEN;
    }

    public String getWorkAR() {
        return workAR;
    }

    public void setWorkAR(String workAR) {
        this.workAR = workAR;
    }

    public String getIsSmokeEN() {
        return isSmokeEN;
    }

    public void setIsSmokeEN(String isSmokeEN) {
        this.isSmokeEN = isSmokeEN;
    }

    public String getIsSmokeAR() {
        return isSmokeAR;
    }

    public void setIsSmokeAR(String isSmokeAR) {
        this.isSmokeAR = isSmokeAR;
    }

    public String getSkinColorEN() {
        return skinColorEN;
    }

    public void setSkinColorEN(String skinColorEN) {
        this.skinColorEN = skinColorEN;
    }

    public String getSkinColorAR() {
        return skinColorAR;
    }

    public void setSkinColorAR(String skinColorAR) {
        this.skinColorAR = skinColorAR;
    }

    public String getHairColorEN() {
        return hairColorEN;
    }

    public void setHairColorEN(String hairColorEN) {
        this.hairColorEN = hairColorEN;
    }

    public String getHairColorAR() {
        return hairColorAR;
    }

    public void setHairColorAR(String hairColorAR) {
        this.hairColorAR = hairColorAR;
    }

    public String getEyeColorEN() {
        return eyeColorEN;
    }

    public void setEyeColorEN(String eyeColorEN) {
        this.eyeColorEN = eyeColorEN;
    }

    public String getEyeColorAR() {
        return eyeColorAR;
    }

    public void setEyeColorAR(String eyeColorAR) {
        this.eyeColorAR = eyeColorAR;
    }

    public String getHeightEN() {
        return heightEN;
    }

    public void setHeightEN(String heightEN) {
        this.heightEN = heightEN;
    }

    public String getHeightAR() {
        return heightAR;
    }

    public void setHeightAR(String heightAR) {
        this.heightAR = heightAR;
    }

    public String getBodyTypeEN() {
        return bodyTypeEN;
    }

    public void setBodyTypeEN(String bodyTypeEN) {
        this.bodyTypeEN = bodyTypeEN;
    }

    public String getBodyTypeAR() {
        return bodyTypeAR;
    }

    public void setBodyTypeAR(String bodyTypeAR) {
        this.bodyTypeAR = bodyTypeAR;
    }

    public String getEducationLevelEN() {
        return educationLevelEN;
    }

    public void setEducationLevelEN(String educationLevelEN) {
        this.educationLevelEN = educationLevelEN;
    }

    public String getEducationLevelAR() {
        return educationLevelAR;
    }

    public void setEducationLevelAR(String educationLevelAR) {
        this.educationLevelAR = educationLevelAR;
    }

    public String getReligionCommitmentEN() {
        return religionCommitmentEN;
    }

    public void setReligionCommitmentEN(String religionCommitmentEN) {
        this.religionCommitmentEN = religionCommitmentEN;
    }

    public String getReligionCommitmentAR() {
        return religionCommitmentAR;
    }

    public void setReligionCommitmentAR(String religionCommitmentAR) {
        this.religionCommitmentAR = religionCommitmentAR;
    }

    public String getFinancialStatusEN() {
        return financialStatusEN;
    }

    public void setFinancialStatusEN(String financialStatusEN) {
        this.financialStatusEN = financialStatusEN;
    }

    public String getFinancialStatusAR() {
        return financialStatusAR;
    }

    public void setFinancialStatusAR(String financialStatusAR) {
        this.financialStatusAR = financialStatusAR;
    }

    public String getSocialStatusEN() {
        return socialStatusEN;
    }

    public void setSocialStatusEN(String socialStatusEN) {
        this.socialStatusEN = socialStatusEN;
    }

    public String getSocialStatusAR() {
        return socialStatusAR;
    }

    public void setSocialStatusAR(String socialStatusAR) {
        this.socialStatusAR = socialStatusAR;
    }

    public String getIsWorkingEN() {
        return isWorkingEN;
    }

    public void setIsWorkingEN(String isWorkingEN) {
        this.isWorkingEN = isWorkingEN;
    }

    public String getIsWorkingAR() {
        return isWorkingAR;
    }

    public void setIsWorkingAR(String isWorkingAR) {
        this.isWorkingAR = isWorkingAR;
    }

    public String getJobTypeEN() {
        return jobTypeEN;
    }

    public void setJobTypeEN(String jobTypeEN) {
        this.jobTypeEN = jobTypeEN;
    }

    public String getJobTypeAR() {
        return jobTypeAR;
    }

    public void setJobTypeAR(String jobTypeAR) {
        this.jobTypeAR = jobTypeAR;
    }

    public String getAnnualIncomeEN() {
        return annualIncomeEN;
    }

    public void setAnnualIncomeEN(String annualIncomeEN) {
        this.annualIncomeEN = annualIncomeEN;
    }

    public String getAnnualIncomeAR() {
        return annualIncomeAR;
    }

    public void setAnnualIncomeAR(String annualIncomeAR) {
        this.annualIncomeAR = annualIncomeAR;
    }

    public String getIsDiseaseEN() {
        return isDiseaseEN;
    }

    public void setIsDiseaseEN(String isDiseaseEN) {
        this.isDiseaseEN = isDiseaseEN;
    }

    public String getIsDiseaseAR() {
        return isDiseaseAR;
    }

    public void setIsDiseaseAR(String isDiseaseAR) {
        this.isDiseaseAR = isDiseaseAR;
    }

    public String getSbrideArrangmentEN() {
        return sbrideArrangmentEN;
    }

    public void setSbrideArrangmentEN(String sbrideArrangmentEN) {
        this.sbrideArrangmentEN = sbrideArrangmentEN;
    }

    public String getSbrideArrangmentAR() {
        return sbrideArrangmentAR;
    }

    public void setSbrideArrangmentAR(String sbrideArrangmentAR) {
        this.sbrideArrangmentAR = sbrideArrangmentAR;
    }

    public int getsCountryId() {
        return sCountryId;
    }

    public void setsCountryId(int sCountryId) {
        this.sCountryId = sCountryId;
    }

    public String getsCountryEN() {
        return sCountryEN;
    }

    public void setsCountryEN(String sCountryEN) {
        this.sCountryEN = sCountryEN;
    }

    public String getsCountryAR() {
        return sCountryAR;
    }

    public void setsCountryAR(String sCountryAR) {
        this.sCountryAR = sCountryAR;
    }

    public String getsNoOfChildrenEN() {
        return sNoOfChildrenEN;
    }

    public void setsNoOfChildrenEN(String sNoOfChildrenEN) {
        this.sNoOfChildrenEN = sNoOfChildrenEN;
    }

    public String getsNoOfChildrenAR() {
        return sNoOfChildrenAR;
    }

    public void setsNoOfChildrenAR(String sNoOfChildrenAR) {
        this.sNoOfChildrenAR = sNoOfChildrenAR;
    }

    public String getsSocialStatusEN() {
        return sSocialStatusEN;
    }

    public void setsSocialStatusEN(String sSocialStatusEN) {
        this.sSocialStatusEN = sSocialStatusEN;
    }

    public String getsSocialStatusAR() {
        return sSocialStatusAR;
    }

    public void setsSocialStatusAR(String sSocialStatusAR) {
        this.sSocialStatusAR = sSocialStatusAR;
    }

    public String getsIsWorkingEN() {
        return sIsWorkingEN;
    }

    public void setsIsWorkingEN(String sIsWorkingEN) {
        this.sIsWorkingEN = sIsWorkingEN;
    }

    public String getsIsWorkingAR() {
        return sIsWorkingAR;
    }

    public void setsIsWorkingAR(String sIsWorkingAR) {
        this.sIsWorkingAR = sIsWorkingAR;
    }

    public String getsAgeEN() {
        return sAgeEN;
    }

    public void setsAgeEN(String sAgeEN) {
        this.sAgeEN = sAgeEN;
    }

    public String getsAgeAR() {
        return sAgeAR;
    }

    public void setsAgeAR(String sAgeAR) {
        this.sAgeAR = sAgeAR;
    }

    public String getsHeightEN() {
        return sHeightEN;
    }

    public void setsHeightEN(String sHeightEN) {
        this.sHeightEN = sHeightEN;
    }

    public String getsHeightAR() {
        return sHeightAR;
    }

    public void setsHeightAR(String sHeightAR) {
        this.sHeightAR = sHeightAR;
    }

    public String getsBodyTypeEN() {
        return sBodyTypeEN;
    }

    public void setsBodyTypeEN(String sBodyTypeEN) {
        this.sBodyTypeEN = sBodyTypeEN;
    }

    public String getsBodyTypeAR() {
        return sBodyTypeAR;
    }

    public void setsBodyTypeAR(String sBodyTypeAR) {
        this.sBodyTypeAR = sBodyTypeAR;
    }

    public String getsSkinColorEN() {
        return sSkinColorEN;
    }

    public void setsSkinColorEN(String sSkinColorEN) {
        this.sSkinColorEN = sSkinColorEN;
    }

    public String getsSkinColorAR() {
        return sSkinColorAR;
    }

    public void setsSkinColorAR(String sSkinColorAR) {
        this.sSkinColorAR = sSkinColorAR;
    }

    public String getsEducationLevelEN() {
        return sEducationLevelEN;
    }

    public void setsEducationLevelEN(String sEducationLevelEN) {
        this.sEducationLevelEN = sEducationLevelEN;
    }

    public String getsEducationLevelAR() {
        return sEducationLevelAR;
    }

    public void setsEducationLevelAR(String sEducationLevelAR) {
        this.sEducationLevelAR = sEducationLevelAR;
    }

    public String getsJobTypeEN() {
        return sJobTypeEN;
    }

    public void setsJobTypeEN(String sJobTypeEN) {
        this.sJobTypeEN = sJobTypeEN;
    }

    public String getsJobTypeAR() {
        return sJobTypeAR;
    }

    public void setsJobTypeAR(String sJobTypeAR) {
        this.sJobTypeAR = sJobTypeAR;
    }

    public String getsCondemnBrideEN() {
        return sCondemnBrideEN;
    }

    public void setsCondemnBrideEN(String sCondemnBrideEN) {
        this.sCondemnBrideEN = sCondemnBrideEN;
    }

    public String getsCondemnBrideAR() {
        return sCondemnBrideAR;
    }

    public void setsCondemnBrideAR(String sCondemnBrideAR) {
        this.sCondemnBrideAR = sCondemnBrideAR;
    }

    public String getsDrivingLicenseEN() {
        return sDrivingLicenseEN;
    }

    public void setsDrivingLicenseEN(String sDrivingLicenseEN) {
        this.sDrivingLicenseEN = sDrivingLicenseEN;
    }

    public String getsDrivingLicenseAR() {
        return sDrivingLicenseAR;
    }

    public void setsDrivingLicenseAR(String sDrivingLicenseAR) {
        this.sDrivingLicenseAR = sDrivingLicenseAR;
    }

    public String getWillPayToBrideEN() {
        return willPayToBrideEN;
    }

    public void setWillPayToBrideEN(String willPayToBrideEN) {
        this.willPayToBrideEN = willPayToBrideEN;
    }

    public String getWillPayToBrideAR() {
        return willPayToBrideAR;
    }

    public void setWillPayToBrideAR(String willPayToBrideAR) {
        this.willPayToBrideAR = willPayToBrideAR;
    }

    public String getFirstRelativeRelationEN() {
        return firstRelativeRelationEN;
    }

    public void setFirstRelativeRelationEN(String firstRelativeRelationEN) {
        this.firstRelativeRelationEN = firstRelativeRelationEN;
    }

    public String getFirstRelativeRelationAR() {
        return firstRelativeRelationAR;
    }

    public void setFirstRelativeRelationAR(String firstRelativeRelationAR) {
        this.firstRelativeRelationAR = firstRelativeRelationAR;
    }

    public String getSecondRelativeRelationEN() {
        return secondRelativeRelationEN;
    }

    public void setSecondRelativeRelationEN(String secondRelativeRelationEN) {
        this.secondRelativeRelationEN = secondRelativeRelationEN;
    }

    public String getSecondRelativeRelationAR() {
        return secondRelativeRelationAR;
    }

    public void setSecondRelativeRelationAR(String secondRelativeRelationAR) {
        this.secondRelativeRelationAR = secondRelativeRelationAR;
    }

    public String getPaymentStatusEN() {
        return paymentStatusEN;
    }

    public void setPaymentStatusEN(String paymentStatusEN) {
        this.paymentStatusEN = paymentStatusEN;
    }

    public String getPaymentStatusAR() {
        return paymentStatusAR;
    }

    public void setPaymentStatusAR(String paymentStatusAR) {
        this.paymentStatusAR = paymentStatusAR;
    }

    public String getSectEN() {
        return sectEN;
    }

    public void setSectEN(String sectEN) {
        this.sectEN = sectEN;
    }

    public String getSectAR() {
        return sectAR;
    }

    public void setSectAR(String sectAR) {
        this.sectAR = sectAR;
    }

    public String getHairTypeEN() {
        return hairTypeEN;
    }

    public void setHairTypeEN(String hairTypeEN) {
        this.hairTypeEN = hairTypeEN;
    }

    public String getHairTypeAR() {
        return hairTypeAR;
    }

    public void setHairTypeAR(String hairTypeAR) {
        this.hairTypeAR = hairTypeAR;
    }

    public String getMemberLicenseIdEN() {
        return memberLicenseIdEN;
    }

    public void setMemberLicenseIdEN(String memberLicenseIdEN) {
        this.memberLicenseIdEN = memberLicenseIdEN;
    }

    public String getMemberLicenseIdAR() {
        return memberLicenseIdAR;
    }

    public void setMemberLicenseIdAR(String memberLicenseIdAR) {
        this.memberLicenseIdAR = memberLicenseIdAR;
    }

    public String getMemberHasChildIdEN() {
        return memberHasChildIdEN;
    }

    public void setMemberHasChildIdEN(String memberHasChildIdEN) {
        this.memberHasChildIdEN = memberHasChildIdEN;
    }

    public String getMemberHasChildIdAR() {
        return memberHasChildIdAR;
    }

    public void setMemberHasChildIdAR(String memberHasChildIdAR) {
        this.memberHasChildIdAR = memberHasChildIdAR;
    }

    public String getMemberReproductionIdEN() {
        return memberReproductionIdEN;
    }

    public void setMemberReproductionIdEN(String memberReproductionIdEN) {
        this.memberReproductionIdEN = memberReproductionIdEN;
    }

    public String getMemberReproductionIdAR() {
        return memberReproductionIdAR;
    }

    public void setMemberReproductionIdAR(String memberReproductionIdAR) {
        this.memberReproductionIdAR = memberReproductionIdAR;
    }

//    public int getReproductionStatusId() {
//        return reproductionStatusId;
//    }
//
//    public void setReproductionStatusId(int reproductionStatusId) {
//        this.reproductionStatusId = reproductionStatusId;
//    }
//
//    public String getReproductionStatusEN() {
//        return reproductionStatusEN;
//    }
//
//    public void setReproductionStatusEN(String reproductionStatusEN) {
//        this.reproductionStatusEN = reproductionStatusEN;
//    }
//
//    public String getReproductionStatusAR() {
//        return reproductionStatusAR;
//    }
//
//    public void setReproductionStatusAR(String reproductionStatusAR) {
//        this.reproductionStatusAR = reproductionStatusAR;
//    }

    public String getMemberCondemnIdEN() {
        return memberCondemnIdEN;
    }

    public void setMemberCondemnIdEN(String memberCondemnIdEN) {
        this.memberCondemnIdEN = memberCondemnIdEN;
    }

    public String getMemberCondemnIdAR() {
        return memberCondemnIdAR;
    }

    public void setMemberCondemnIdAR(String memberCondemnIdAR) {
        this.memberCondemnIdAR = memberCondemnIdAR;
    }

    public String getMemberNoOfChildrenIdEN() {
        return memberNoOfChildrenIdEN;
    }

    public void setMemberNoOfChildrenIdEN(String memberNoOfChildrenIdEN) {
        this.memberNoOfChildrenIdEN = memberNoOfChildrenIdEN;
    }

    public String getMemberNoOfChildrenIdAR() {
        return memberNoOfChildrenIdAR;
    }

    public void setMemberNoOfChildrenIdAR(String memberNoOfChildrenIdAR) {
        this.memberNoOfChildrenIdAR = memberNoOfChildrenIdAR;
    }

    public String getsStateEN() {
        return sStateEN;
    }

    public void setsStateEN(String sStateEN) {
        this.sStateEN = sStateEN;
    }

    public String getsStateAR() {
        return sStateAR;
    }

    public void setsStateAR(String sStateAR) {
        this.sStateAR = sStateAR;
    }

    public int getSpouseStateToLiveId() {
        return spouseStateToLiveId;
    }

    public void setSpouseStateToLiveId(int spouseStateToLiveId) {
        this.spouseStateToLiveId = spouseStateToLiveId;
    }

    public String getSpouseStateToLiveEN() {
        return spouseStateToLiveEN;
    }

    public void setSpouseStateToLiveEN(String spouseStateToLiveEN) {
        this.spouseStateToLiveEN = spouseStateToLiveEN;
    }

    public String getSpouseStateToLiveAR() {
        return spouseStateToLiveAR;
    }

    public void setSpouseStateToLiveAR(String spouseStateToLiveAR) {
        this.spouseStateToLiveAR = spouseStateToLiveAR;
    }

    public String getsHasChildIdEN() {
        return sHasChildIdEN;
    }

    public void setsHasChildIdEN(String sHasChildIdEN) {
        this.sHasChildIdEN = sHasChildIdEN;
    }

    public String getsHasChildIdAR() {
        return sHasChildIdAR;
    }

    public void setsHasChildIdAR(String sHasChildIdAR) {
        this.sHasChildIdAR = sHasChildIdAR;
    }

    public String getMotherNationalityEN() {
        return motherNationalityEN;
    }

    public void setMotherNationalityEN(String motherNationalityEN) {
        this.motherNationalityEN = motherNationalityEN;
    }

    public String getMotherNationalityAR() {
        return motherNationalityAR;
    }

    public void setMotherNationalityAR(String motherNationalityAR) {
        this.motherNationalityAR = motherNationalityAR;
    }

    public String getResidenceTypeEN() {
        return residenceTypeEN;
    }

    public void setResidenceTypeEN(String residenceTypeEN) {
        this.residenceTypeEN = residenceTypeEN;
    }

    public String getResidenceTypeAR() {
        return residenceTypeAR;
    }

    public void setResidenceTypeAR(String residenceTypeAR) {
        this.residenceTypeAR = residenceTypeAR;
    }

    public String getEthnicityEN() {
        return ethnicityEN;
    }

    public void setEthnicityEN(String ethnicityEN) {
        this.ethnicityEN = ethnicityEN;
    }

    public String getEthnicityAR() {
        return ethnicityAR;
    }

    public void setEthnicityAR(String ethnicityAR) {
        this.ethnicityAR = ethnicityAR;
    }

    public String getMemberWeightEN() {
        return memberWeightEN;
    }

    public void setMemberWeightEN(String memberWeightEN) {
        this.memberWeightEN = memberWeightEN;
    }

    public String getMemberWeightAR() {
        return memberWeightAR;
    }

    public void setMemberWeightAR(String memberWeightAR) {
        this.memberWeightAR = memberWeightAR;
    }

//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }

    @SerializedName("verificationCode")
    @Expose
    private int verificationCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @SerializedName(value="name5",  alternate={"imagePath" , "image"})
    @Expose
    private String imagePath;

    //********************
    //Member Login Details
    //********************

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmiratesIdCardNumber() {
        return emiratesIdCardNumber;
    }

    public void setEmiratesIdCardNumber(String emiratesIdCardNumber) {
        this.emiratesIdCardNumber = emiratesIdCardNumber;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public int getIsSmokingId() {
        return isSmokingId;
    }

    public void setIsSmokingId(int isSmokingId) {
        this.isSmokingId = isSmokingId;
    }

    public int getSkinColorId() {
        return skinColorId;
    }

    public void setSkinColorId(int skinColorId) {
        this.skinColorId = skinColorId;
    }

    public int getHairColorId() {
        return hairColorId;
    }

    public void setHairColorId(int hairColorId) {
        this.hairColorId = hairColorId;
    }

    public int getEyeColorId() {
        return eyeColorId;
    }

    public void setEyeColorId(int eyeColorId) {
        this.eyeColorId = eyeColorId;
    }

    public int getHeightId() {
        return heightId;
    }

    public void setHeightId(int heightId) {
        this.heightId = heightId;
    }

    public int getBodyTypeId() {
        return bodyTypeId;
    }

    public void setBodyTypeId(int bodyTypeId) {
        this.bodyTypeId = bodyTypeId;
    }

    public int getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(int educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public int getReligionCommitmentId() {
        return religionCommitmentId;
    }

    public void setReligionCommitmentId(int religionCommitmentId) {
        this.religionCommitmentId = religionCommitmentId;
    }

    public int getFinancialStatusId() {
        return financialStatusId;
    }

    public void setFinancialStatusId(int financialStatusId) {
        this.financialStatusId = financialStatusId;
    }

    public int getSocialStatusId() {
        return socialStatusId;
    }

    public void setSocialStatusId(int socialStatusId) {
        this.socialStatusId = socialStatusId;
    }

    public int getIsWorkingId() {
        return isWorkingId;
    }

    public void setIsWorkingId(int isWorkingId) {
        this.isWorkingId = isWorkingId;
    }

    public int getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(int jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getAnnualIncomeId() {
        return annualIncomeId;
    }

    public void setAnnualIncomeId(int annualIncomeId) {
        this.annualIncomeId = annualIncomeId;
    }

    public int getIsDiseaseId() {
        return isDiseaseId;
    }

    public void setIsDiseaseId(int isDiseaseId) {
        this.isDiseaseId = isDiseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public int getLookingForId() {
        return lookingForId;
    }

    public void setLookingForId(int lookingForId) {
        this.lookingForId = lookingForId;
    }

    public int getSbrideArrangmentId() {
        return sbrideArrangmentId;
    }

    public void setSbrideArrangmentId(int sbrideArrangmentId) {
        this.sbrideArrangmentId = sbrideArrangmentId;
    }

    public int getsNationalityId() {
        return sCountryId;
    }

    public void setsNationalityId(int sNationalityId) {
        this.sCountryId = sNationalityId;
    }

    public int getSnoOfChildrenId() {
        return snoOfChildrenId;
    }

    public void setSnoOfChildrenId(int snoOfChildrenId) {
        this.snoOfChildrenId = snoOfChildrenId;
    }

    public int getsReproductionId() {
        return sReproductionId;
    }

    public void setsReproductionId(int sReproductionId) {
        this.sReproductionId = sReproductionId;
    }

    public int getsSocialStatusId() {
        return sSocialStatusId;
    }

    public void setsSocialStatusId(int sSocialStatusId) {
        this.sSocialStatusId = sSocialStatusId;
    }

    public int getsIsWorkingId() {
        return sIsWorkingId;
    }

    public void setsIsWorkingId(int sIsWorkingId) {
        this.sIsWorkingId = sIsWorkingId;
    }

    public int getsAgeId() {
        return sAgeId;
    }

    public void setsAgeId(int sAgeId) {
        this.sAgeId = sAgeId;
    }

    public int getsHeightId() {
        return sHeightId;
    }

    public void setsHeightId(int sHeightId) {
        this.sHeightId = sHeightId;
    }

    public int getsBodyTypeId() {
        return sBodyTypeId;
    }

    public void setsBodyTypeId(int sBodyTypeId) {
        this.sBodyTypeId = sBodyTypeId;
    }

    public int getsSkinColorId() {
        return sSkinColorId;
    }

    public void setsSkinColorId(int sSkinColorId) {
        this.sSkinColorId = sSkinColorId;
    }

    public int getsEducationLevelId() {
        return sEducationLevelId;
    }

    public void setsEducationLevelId(int sEducationLevelId) {
        this.sEducationLevelId = sEducationLevelId;
    }

    public int getsJobTypeId() {
        return sJobTypeId;
    }

    public void setsJobTypeId(int sJobTypeId) {
        this.sJobTypeId = sJobTypeId;
    }

    public int getsCondemnBrideId() {
        return sCondemnBrideId;
    }

    public void setsCondemnBrideId(int sCondemnBrideId) {
        this.sCondemnBrideId = sCondemnBrideId;
    }

    public int getsDrivingLicenseId() {
        return sDrivingLicenseId;
    }

    public void setsDrivingLicenseId(int sDrivingLicenseId) {
        this.sDrivingLicenseId = sDrivingLicenseId;
    }

    public int getsWillPayToBrideId() {
        return sWillPayToBrideId;
    }

    public void setsWillPayToBrideId(int sWillPayToBrideId) {
        this.sWillPayToBrideId = sWillPayToBrideId;
    }

    public String getFirstRelative() {
        return firstRelative;
    }

    public void setFirstRelative(String firstRelative) {
        this.firstRelative = firstRelative;
    }

    public String getFirstRelativeNumber() {
        return firstRelativeNumber;
    }

    public void setFirstRelativeNumber(String firstRelativeNumber) {
        this.firstRelativeNumber = firstRelativeNumber;
    }

    public int getFirstRelativeRelationId() {
        return firstRelativeRelationId;
    }

    public void setFirstRelativeRelationId(int firstRelativeRelationId) {
        this.firstRelativeRelationId = firstRelativeRelationId;
    }

    public String getSecondRelative() {
        return secondRelative;
    }

    public void setSecondRelative(String secondRelative) {
        this.secondRelative = secondRelative;
    }

    public String getSecondRelativeNumber() {
        return secondRelativeNumber;
    }

    public void setSecondRelativeNumber(String secondRelativeNumber) {
        this.secondRelativeNumber = secondRelativeNumber;
    }

    public int getSecondRelativeRelationId() {
        return secondRelativeRelationId;
    }

    public void setSecondRelativeRelationId(int secondRelativeRelationId) {
        this.secondRelativeRelationId = secondRelativeRelationId;
    }

    public String getApplicantDescription() {
        return applicantDescription;
    }

    public void setApplicantDescription(String applicantDescription) {
        this.applicantDescription = applicantDescription;
    }

    public String getLanguagesId() {
        return languagesId == null ? "" : languagesId;
    }

    public void setLanguagesId(String languagesId) {
        this.languagesId = languagesId;
    }

    public int getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(int paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(int mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public int getSectTypeId() {
        return sectTypeId;
    }

    public void setSectTypeId(int sectTypeId) {
        this.sectTypeId = sectTypeId;
    }

    public int getHairTypeId() {
        return hairTypeId;
    }

    public void setHairTypeId(int hairTypeId) {
        this.hairTypeId = hairTypeId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserUpdateId() {
        return userUpdateId;
    }

    public void setUserUpdateId(int userUpdateId) {
        this.userUpdateId = userUpdateId;
    }

    public String getcAdress() {
        return cAdress;
    }

    public void setcAdress(String cAdress) {
        this.cAdress = cAdress;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public int getMemberLicenseId() {
        return memberLicenseId;
    }

    public void setMemberLicenseId(int memberLicenseId) {
        this.memberLicenseId = memberLicenseId;
    }

    public int getMemberHasChildId() {
        return memberHasChildId;
    }

    public void setMemberHasChildId(int memberHasChildId) {
        this.memberHasChildId = memberHasChildId;
    }

    public int getMemberReproductionId() {
        return memberReproductionId;
    }

    public void setMemberReproductionId(int memberReproductionId) {
        this.memberReproductionId = memberReproductionId;
    }

    public int getMemberCondemnId() {
        return memberCondemnId;
    }

    public void setMemberCondemnId(int memberCondemnId) {
        this.memberCondemnId = memberCondemnId;
    }

    public int getMemberNoOfChildrenId() {
        return memberNoOfChildrenId;
    }

    public void setMemberNoOfChildrenId(int memberNoOfChildrenId) {
        this.memberNoOfChildrenId = memberNoOfChildrenId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String memberWorking) {
        this.occupation = memberWorking;
    }

    public int getsRequiredStateId() {
        return sRequiredStateId;
    }

    public void setsRequiredStateId(int sRequiredStateId) {
        this.sRequiredStateId = sRequiredStateId;
    }

    public int getsHasChildId() {
        return sHasChildId;
    }

    public void setsHasChildId(int sHasChildId) {
        this.sHasChildId = sHasChildId;
    }

    public boolean getIsPolygamy() {
        return isPolygamy == null ? false : isPolygamy;
    }

    public void setIsPolygamy(boolean isPolygamy) {
        this.isPolygamy = isPolygamy;
    }

    public int getMotherNationalityId() {
        return motherNationalityId;
    }

    public void setMotherNationalityId(int motherNationalityId) {
        this.motherNationalityId = motherNationalityId;
    }

    public int getResidenceTypeId() {
        return residenceTypeId;
    }

    public void setResidenceTypeId(int residenceTypeId) {
        this.residenceTypeId = residenceTypeId;
    }

    public int getEthnicityId() {
        return ethnicityId;
    }

    public void setEthnicityId(int ethnicityId) {
        this.ethnicityId = ethnicityId;
    }

    public Boolean getIsFamilyShow() {
        return isFamilyShow == null ? true : isFamilyShow;
    }

    public void setIsFamilyShow(Boolean isFamilyShow) {
        this.isFamilyShow = (isFamilyShow == null ? true : isFamilyShow);
    }

    public Boolean getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(Boolean isViewed) {
        this.isViewed = isViewed;
    }

    public int getMemberWeightId() {
        return memberWeightId;
    }

    public void setMemberWeightId(int memberWeightId) {
        this.memberWeightId = memberWeightId;
    }

    @SerializedName("memberId")
    @Expose
    private int memberId;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("emirateId")
    @Expose
    private String emiratesIdCardNumber;

    @SerializedName("family")
    @Expose
    private String family;

    @SerializedName("birthDate")
    @Expose
    private String birthDate;

    @SerializedName("birthPlace")
    @Expose
    private String birthPlace;

    @SerializedName("countryId")
    @Expose
    private int countryId;
    @SerializedName("countryEN")
    @Expose
    private String countryEN;
    @SerializedName("countryAR")
    @Expose
    private String countryAR;

    @SerializedName("workId")
    @Expose
    private int workId;
    @SerializedName("workEN")
    @Expose
    private String workEN;
    @SerializedName("workAR")
    @Expose
    private String workAR;

    @SerializedName(value="name8",  alternate={"isSmokingId" ,"isSmokeId"  })
    @Expose
    private int isSmokingId;
    @SerializedName("isSmokeEN")
    @Expose
    private String isSmokeEN;
    @SerializedName("isSmokeAR")
    @Expose
    private String isSmokeAR;

    @SerializedName("skinColorId")
    @Expose
    private int skinColorId;
    @SerializedName("skinColorEN")
    @Expose
    private String skinColorEN;
    @SerializedName("skinColorAR")
    @Expose
    private String skinColorAR;

    @SerializedName("hairColorId")
    @Expose
    private int hairColorId;
    @SerializedName("hairColorEN")
    @Expose
    private String hairColorEN;
    @SerializedName("hairColorAR")
    @Expose
    private String hairColorAR;

    @SerializedName("eyeColorId")
    @Expose
    private int eyeColorId;
    @SerializedName("eyeColorEN")
    @Expose
    private String eyeColorEN;
    @SerializedName("eyeColorAR")
    @Expose
    private String eyeColorAR;

    @SerializedName("heightId")
    @Expose
    private int heightId;
    @SerializedName("heightEN")
    @Expose
    private String heightEN;
    @SerializedName("heightAR")
    @Expose
    private String heightAR;

    @SerializedName("bodyTypeId")
    @Expose
    private int bodyTypeId;
    @SerializedName("bodyTypeEN")
    @Expose
    private String bodyTypeEN;
    @SerializedName("bodyTypeAR")
    @Expose
    private String bodyTypeAR;

    @SerializedName("educationLevelId")
    @Expose
    private int educationLevelId;
    @SerializedName("educationLevelEN")
    @Expose
    private String educationLevelEN;
    @SerializedName("educationLevelAR")
    @Expose
    private String educationLevelAR;

    @SerializedName("religionCommitmentId")
    @Expose
    private int religionCommitmentId;
    @SerializedName("religionCommitmentEN")
    @Expose
    private String religionCommitmentEN;
    @SerializedName("religionCommitmentAR")
    @Expose
    private String religionCommitmentAR;

    @SerializedName("financialStatusId")
    @Expose
    private int financialStatusId;
    @SerializedName("financialStatusEN")
    @Expose
    private String financialStatusEN;
    @SerializedName("financialStatusAR")
    @Expose
    private String financialStatusAR;

    @SerializedName("socialStatusId")
    @Expose
    private int socialStatusId;
    @SerializedName("socialStatusEN")
    @Expose
    private String socialStatusEN;
    @SerializedName("socialStatusAR")
    @Expose
    private String socialStatusAR;

    @SerializedName(value="name9",  alternate={"isWorkingId" ,"workStatusId"  })
    @Expose
    private int isWorkingId;
    @SerializedName("workStatusEN")
    @Expose
    private String isWorkingEN;
    @SerializedName("workStatusAR")
    @Expose
    private String isWorkingAR;

    @SerializedName("jobTypeId")
    @Expose
    private int jobTypeId;
    @SerializedName("jobTypeEN")
    @Expose
    private String jobTypeEN;
    @SerializedName("jobTypeAR")
    @Expose
    private String jobTypeAR;

    @SerializedName("jobTitle")
    @Expose
    private String jobTitle;

    @SerializedName("annualIncomeId")
    @Expose
    private int annualIncomeId;
    @SerializedName("annualIncomeEN")
    @Expose
    private String annualIncomeEN;
    @SerializedName("annualIncomeAR")
    @Expose
    private String annualIncomeAR;

    @SerializedName("isDiseaseId")
    @Expose
    private int isDiseaseId;
    @SerializedName("isDiseaseEN")
    @Expose
    private String isDiseaseEN;
    @SerializedName("isDiseaseAR")
    @Expose
    private String isDiseaseAR;

    @SerializedName("diseaseName")
    @Expose
    private String diseaseName;

    @SerializedName("lookingForId")
    @Expose
    private int lookingForId;

    @SerializedName("sbrideArrangmentId")
    @Expose
    private int sbrideArrangmentId;
    @SerializedName("sbrideArrangmentEN")
    @Expose
    private String sbrideArrangmentEN;
    @SerializedName("sbrideArrangmentAR")
    @Expose
    private String sbrideArrangmentAR;

    @SerializedName(value="name14",  alternate={"sNationalityId" ,"sCountryId" })
    @Expose
    private int sCountryId;
    @SerializedName("sCountryEN")
    @Expose
    private String sCountryEN;
    @SerializedName("sCountryAR")
    @Expose
    private String sCountryAR;

    @SerializedName(value="name11",  alternate={"snoOfChildrenId" ,"noOfChildrenId" })
    @Expose
    private int snoOfChildrenId;
    @SerializedName("sNoOfChildrenEN")
    @Expose
    private String sNoOfChildrenEN;
    @SerializedName("sNoOfChildrenAR")
    @Expose
    private String sNoOfChildrenAR;

    @SerializedName(value="name19",  alternate={"sReproductionId" ,"reproductionStatusId" })
    @Expose
    private int sReproductionId;

    @SerializedName(value="name20",  alternate={"sReproductionStatusEN" ,"reproductionStatusEN" })
    @Expose
    private String sReproductionStatusEN;
    public String getsReproductionStatusEN() {
        return sReproductionStatusEN;
    }

    public void setsReproductionStatusEN(String sReproductionStatusEN) {
        this.sReproductionStatusEN = sReproductionStatusEN;
    }

    //
    @SerializedName(value="name21",  alternate={"sReproductionStatusAR" ,"reproductionStatusAR" })
    @Expose
    private String sReproductionStatusAR;
    public String getsReproductionStatusAR() {
        return sReproductionStatusAR;
    }

    public void setsReproductionStatusAR(String sReproductionStatusAR) {
        this.sReproductionStatusAR = sReproductionStatusAR;
    }

    @SerializedName("sSocialStatusId")
    @Expose
    private int sSocialStatusId;
    @SerializedName("sSocialStatusEN")
    @Expose
    private String sSocialStatusEN;
    @SerializedName("sSocialStatusAR")
    @Expose
    private String sSocialStatusAR;


    @SerializedName("sIsWorkingId")
    @Expose
    private int sIsWorkingId;
    @SerializedName("sIsWorkingEN")
    @Expose
    private String sIsWorkingEN;
    @SerializedName("sIsWorkingAR")
    @Expose
    private String sIsWorkingAR;


    @SerializedName("sAgeId")
    @Expose
    private int sAgeId;
    @SerializedName("sAgeEN")
    @Expose
    private String sAgeEN;
    @SerializedName("sAgeAR")
    @Expose
    private String sAgeAR;

    @SerializedName("sHeightId")
    @Expose
    private int sHeightId;
    @SerializedName("sHeightEN")
    @Expose
    private String sHeightEN;
    @SerializedName("sHeightAR")
    @Expose
    private String sHeightAR;

    @SerializedName("sBodyTypeId")
    @Expose
    private int sBodyTypeId;
    @SerializedName("sBodyTypeEN")
    @Expose
    private String sBodyTypeEN;
    @SerializedName("sBodyTypeAR")
    @Expose
    private String sBodyTypeAR;

    @SerializedName("sSkinColorId")
    @Expose
    private int sSkinColorId;
    @SerializedName("sSkinColorEN")
    @Expose
    private String sSkinColorEN;
    @SerializedName("sSkinColorAR")
    @Expose
    private String sSkinColorAR;

    @SerializedName("sEducationLevelId")
    @Expose
    private int sEducationLevelId;
    @SerializedName("sEducationLevelEN")
    @Expose
    private String sEducationLevelEN;
    @SerializedName("sEducationLevelAR")
    @Expose
    private String sEducationLevelAR;


    @SerializedName(value="name16",  alternate={"sJobTypeId" ,"sOccuptionId"  })
    @Expose
    private int sJobTypeId;
    @SerializedName("sOccuptionEN")
    @Expose
    private String sJobTypeEN;
    @SerializedName("sOccuptionAR")
    @Expose
    private String sJobTypeAR;

    @SerializedName("sCondemnBrideId")
    @Expose
    private int sCondemnBrideId;
    @SerializedName("sCondemnBrideEN")
    @Expose
    private String sCondemnBrideEN;
    @SerializedName("sCondemnBrideAR")
    @Expose
    private String sCondemnBrideAR;

    @SerializedName("sDrivingLicenseId")
    @Expose
    private int sDrivingLicenseId;
    @SerializedName("sDrivingLicenseEN")
    @Expose
    private String sDrivingLicenseEN;
    @SerializedName("sDrivingLicenseAR")
    @Expose
    private String sDrivingLicenseAR;

    @SerializedName(value="name17",  alternate={"sWillPayToBrideId" ,"willPayToBrideId"  })
    @Expose
    private int sWillPayToBrideId;
    @SerializedName("willPayToBrideEN")
    @Expose
    private String willPayToBrideEN;
    @SerializedName("willPayToBrideAR")
    @Expose
    private String willPayToBrideAR;

    @SerializedName("firstRelative")
    @Expose
    private String firstRelative;

    @SerializedName("firstRelativeNumber")
    @Expose
    private String firstRelativeNumber;

    @SerializedName("firstRelativeRelationId")
    @Expose
    private int firstRelativeRelationId;
    @SerializedName("firstRelativeRelationEN")
    @Expose
    private String firstRelativeRelationEN;
    @SerializedName("firstRelativeRelationAR")
    @Expose
    private String firstRelativeRelationAR;

    @SerializedName("secondRelative")
    @Expose
    private String secondRelative;

    @SerializedName("secondRelativeNumber")
    @Expose
    private String secondRelativeNumber;

    @SerializedName("secondRelativeRelationId")
    @Expose
    private int secondRelativeRelationId;
    @SerializedName("secondRelativeRelationEN")
    @Expose
    private String secondRelativeRelationEN;
    @SerializedName("secondRelativeRelationAR")
    @Expose
    private String secondRelativeRelationAR;

    @SerializedName("applicantDescription")
    @Expose
    private String applicantDescription;

    @SerializedName("languagesId")
    @Expose
    private String languagesId;

    @SerializedName("paymentStatusId")
    @Expose
    private int paymentStatusId;
    @SerializedName(value="name10",  alternate={"paymentStatusEN" ,"statusEN" })
    @Expose
    private String paymentStatusEN;
    @SerializedName(value="name18",  alternate={"paymentStatusAR" ,"statusAR" })
    @Expose
    private String paymentStatusAR;

    @SerializedName("signature")
    @Expose
    private String signature;

    @SerializedName("mobileStatus")
    @Expose
    private int mobileStatus;

    @SerializedName("sectTypeId")
    @Expose
    private int sectTypeId;
    @SerializedName("sectEN")
    @Expose
    private String sectEN;
    @SerializedName("sectAR")
    @Expose
    private String sectAR;


    @SerializedName("hairTypeId")
    @Expose
    private int hairTypeId;
    @SerializedName("hairTypeEN")
    @Expose
    private String hairTypeEN;
    @SerializedName("hairTypeAR")
    @Expose
    private String hairTypeAR;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("userUpdateId")
    @Expose
    private int userUpdateId;

    @SerializedName("cAdress")
    @Expose
    private String cAdress;


    @SerializedName("matchCount")
    @Expose
    private int matchCount;

    @SerializedName("memberLicenseId")
    @Expose
    private int memberLicenseId;
    @SerializedName("memberLicenseIdEN")
    @Expose
    private String memberLicenseIdEN;
    @SerializedName("memberLicenseIdAR")
    @Expose
    private String memberLicenseIdAR;

    @SerializedName("memberHasChildId")
    @Expose
    private int memberHasChildId;
    @SerializedName("memberHasChildIdEN")
    @Expose
    private String memberHasChildIdEN;
    @SerializedName("memberHasChildIdAR")
    @Expose
    private String memberHasChildIdAR;

    @SerializedName("memberReproductionId")
    @Expose
    private int memberReproductionId;
    @SerializedName("memberReproductionIdEN")
    @Expose
    private String memberReproductionIdEN;
    @SerializedName("memberReproductionIdAR")
    @Expose
    private String memberReproductionIdAR;

//    @SerializedName("reproductionStatusId")
//    @Expose
//    private int reproductionStatusId;
//
//    @SerializedName("reproductionStatusEN")
//    @Expose
//    private String reproductionStatusEN;
//
//    @SerializedName("reproductionStatusAR")
//    @Expose
//    private String reproductionStatusAR;

    @SerializedName("memberCondemnId")
    @Expose
    private int memberCondemnId;
    @SerializedName("memberCondemnIdEN")
    @Expose
    private String memberCondemnIdEN;
    @SerializedName("memberCondemnIdAR")
    @Expose
    private String memberCondemnIdAR;

    @SerializedName("memberNoOfChildrenId")
    @Expose
    private int memberNoOfChildrenId;
    @SerializedName("memberNoOfChildrenIdEN")
    @Expose
    private String memberNoOfChildrenIdEN;
    @SerializedName("memberNoOfChildrenIdAR")
    @Expose
    private String memberNoOfChildrenIdAR;

    @SerializedName("memberWorking")
    @Expose
    private String occupation;

    @SerializedName(value="name13",  alternate={"sRequiredStateId" ,"sStateId" })
    @Expose
    private int sRequiredStateId;
    @SerializedName("sStateEN")
    @Expose
    private String sStateEN;
    @SerializedName("sStateAR")
    @Expose
    private String sStateAR;

    @SerializedName("spouseStateToLiveId")
    @Expose
    private int spouseStateToLiveId;
    @SerializedName("spouseStateToLiveEN")
    @Expose
    private String spouseStateToLiveEN;
    @SerializedName("spouseStateToLiveAR")
    @Expose
    private String spouseStateToLiveAR;

    @SerializedName("sHasChildId")
    @Expose
    private int sHasChildId;
    @SerializedName("sHasChildIdEN")
    @Expose
    private String sHasChildIdEN;
    @SerializedName("sHasChildIdAR")
    @Expose
    private String sHasChildIdAR;


    @SerializedName("isPolygamy")
    @Expose
    private Boolean isPolygamy;

    @SerializedName("motherNationalityId")
    @Expose
    private int motherNationalityId;
    @SerializedName("motherNationalityEN")
    @Expose
    private String motherNationalityEN;
    @SerializedName("motherNationalityAR")
    @Expose
    private String motherNationalityAR;

    @SerializedName("residenceTypeId")
    @Expose
    private int residenceTypeId;
    @SerializedName("residenceTypeEN")
    @Expose
    private String residenceTypeEN;
    @SerializedName("residenceTypeAR")
    @Expose
    private String residenceTypeAR;

    @SerializedName("ethnicityId")
    @Expose
    private int ethnicityId;
    @SerializedName("ethnicityEN")
    @Expose
    private String ethnicityEN;
    @SerializedName("ethnicityAR")
    @Expose
    private String ethnicityAR;

    @SerializedName("isFamilyShow")
    @Expose
    private Boolean isFamilyShow = true;

    @SerializedName("isViewed")
    @Expose
    private Boolean isViewed;

    @SerializedName("memberWeightId")
    @Expose
    private int memberWeightId;
    @SerializedName("memberWeightEN")
    @Expose
    private String memberWeightEN;

    @SerializedName("memberWeightAR")
    @Expose
    private String memberWeightAR;

    @SerializedName("GCCMarriage")
    @Expose
    private Boolean GCCMarriage;


    @SerializedName("acceptDMW")
    @Expose
    private Boolean acceptDMW;

    public Boolean getAcceptDMW() {
        return acceptDMW;
    }

    public void setAcceptDMW(Boolean acceptDMW) {
        this.acceptDMW = acceptDMW;
    }

    public Boolean getGCCMarriage() {
        return GCCMarriage;
    }

    public void setGCCMarriage(Boolean GCCMarriage) {
        this.GCCMarriage = GCCMarriage;
    }

    public String getReferName() {
        return referName;
    }

    public void setReferName(String referName) {
        this.referName = referName;
    }

    @SerializedName("nameRefer")
    @Expose
    private String referName;

    public String getReferMobile() {
        return referMobile;
    }

    public void setReferMobile(String referMobile) {
        this.referMobile = referMobile;
    }


    @SerializedName("mobileRefer")
    @Expose
    private String referMobile;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = consultantId;
    }

    public String getStateId() {
        return stateId == null ? "" : stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getConsulantLastName() {
        return consulantLastName;
    }

    public void setConsulantLastName(String consulantLastName) {
        this.consulantLastName = consulantLastName;
    }

    public String getStateServeIn() {
        return stateServeIn;
    }

    public void setStateServeIn(String stateServeIn) {
        this.stateServeIn = stateServeIn;
    }
}
