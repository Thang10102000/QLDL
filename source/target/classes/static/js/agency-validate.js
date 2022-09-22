$(".select2").select2();
$(document).ready(function() {
    $('#sidebarCollapse').on('click', function() {
        $('#sidebar').toggleClass('active');
    });
    $("#licenseDate").datepicker({
        format : "dd/mm/yyyy",
        todayHighlight : true,
        endDate: 'today',
        inline : true
    });
    $("#birthdayRepresent").datepicker({
        format: "dd/mm/yyyy",
        todayHighlight: true,
        endDate: 'today'
    });
    $("#birthdayAuthorized").datepicker({
        format: "dd/mm/yyyy",
        todayHighlight: true,
        endDate: 'today'
    });
    $("#birthdayContact").datepicker({
        format: "dd/mm/yyyy",
        todayHighlight: true,
        endDate: 'today'
    });
    $("#agencyName , #businessLicense, #licenseDate, #licensePlace, #taxCode, #phoneNumber, #email, #agencyCode").keydown(function () {
        $("#agencyNameMessage, #businessLicenseMessage, #licenseDateMessage, #licensePlaceMessage, #taxCodeMessage, #phoneNumberMessage, #emailMessage, #agencyCodeMessage").text("");
    })
    $("#fullNameRepresent, #emailRepresent, #telephoneRepresent, #phoneNumberRepresent").keydown(function () {
        $("#fullNameRepresentMessage, #emailRepresentMessage, #telephoneRepresentMessage, #phoneNumberRepresentMessage").text("");
    })
    $("#fullNameContact, #emailContact, #telephoneContact, #phoneNumberContact").keydown(function () {
        $("#fullNameContactMessage, #emailContactMessage, #telephoneContactMessage, #phoneNumberContactMessage").text("");
    })
});
$(document).on('click','#doNew',function(e) {
    var format = /[`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    // var vnf_regex = /((09|03|02|84|07|08|05)+([0-9]{5,11})\b)/g;
    var vnf_regex = /(([0-9]{7,13})\b)/g;
    var regex_ac = /^[a-zA-Z0-9]{8,10}$/;
    var business_regex = /[a-z0-9A-Z]{10}-[0-9a-zA-Z]{3}/;
    if (format.test($("#agencyName").val())) {
        $("#agencyNameMessage").text("Tên đại lý không được chứa ký tự đặc biệt");
        $("#agencyName").focus();
        return false;
    }
    if ($("#agencyName").val().trim() === '') {
        $("#agencyNameMessage").text("Bạn chưa nhập tên đại lý");
        $("#agencyName").focus();
        return false;
    }
    if ($("#businessLicense").val().trim() === '') {
        $("#businessLicenseMessage").text("Bạn chưa nhập số giấy phép kinh doanh");
        $("#businessLicense").focus();
        return false;
    }
    if (!regex_ac.test($("#businessLicense").val())){
        $("#businessLicenseMessage").text("Số giấy phép kinh doanh chưa đúng định dạng");
        $("#businessLicense").focus();
        return false;
    }
    if ($("#licenseDate").val().trim() === '') {
        $("#licenseDateMessage").text("Bạn chưa nhập ngày cấp phép kinh doanh");
        $("#licenseDate").focus();
        return false;
    }
    if ($("#licensePlace").val().trim() === '') {
        $("#licensePlaceMessage").text("Bạn chưa nhập nơi cấp phép kinh doanh");
        $("#licensePlace").focus();
        return false;
    }
    if ($("#taxCode").val().trim() === '') {
        $("#taxCodeMessage").text("Bạn chưa nhập mã số thuế");
        $("#taxCode").focus();
        return false;
    }
    if (!regex_ac.test($("#taxCode").val())) {
        $("#taxCodeMessage").text("Mã số thuế không đúng định dạng");
        $("#taxCode").focus();
        return false;
    }
    if ($("#phoneNumber").val().trim() === '') {
        $("#phoneNumberMessage").text("Bạn chưa nhập số điện thoại");
        $("#phoneNumber").focus();
        return false;
    }
    if (!vnf_regex.test($("#phoneNumber").val())) {
        $("#phoneNumberMessage").text("Số điện thoại sai định dạng");
        $("#phoneNumber").focus();
        return false;
    }
    if ($("#email").val().trim() === '') {
        $("#emailMessage").text("Bạn chưa nhập email");
        $("#email").focus();
        return false;
    }
    if (!re.test($("#email").val().trim())) {
        $("#emailMessage").text("Email không đúng định dạng");
        $("#email").focus();
        return false;
    }
    if ($("#agencyCode").val().trim() === '') {
        $("#agencyCodeMessage").text("Bạn chưa nhập mã đại lý");
        $("#agencyCode").focus();
        return false;
    }
    if (!regex_ac.test($("#agencyCode").val())){
        $("#agencyCodeMessage").text("Mã đại lý chưa đúng định dạng");
        $("#agencyCode").focus();
        return false;
    }

    //	validate thong tin lien he dai ly
    //	thong tin nguoi dai dien
    if (format.test($("#fullNameRepresent").val())) {
        $("#fullNameRepresentMessage").text("Họ và tên không được chứa ký tự đặc biệt");
        $("#fullNameRepresent").focus();
        return false;
    }
    if ($("#fullNameRepresent").val().trim() === '') {
        $("#fullNameRepresentMessage").text("Họ và tên không được để trống");
        $("#fullNameRepresent").focus();
        return false;
    }
    if ($("#emailRepresent").val().trim() === '') {
        $("#emailRepresentMessage").text("Email không được để trống");
        $("#emailRepresent").focus();
        return false;
    }
    if ($("#telephoneRepresent").val().length > 0){
        if ($("#telephoneRepresent").val()*1 != $("#telephoneRepresent").val() || $("#telephoneRepresent").val().length < 7 || $("#telephoneRepresent").val().length > 13) {
            $("#telephoneRepresentMessage").text("Số điện thoại sai định dạng");
            $("#telephoneRepresent").focus();
            return false;
        }
    }
    if ($("#phoneNumberRepresent").val().length > 0){
        if ($("#phoneNumberRepresent").val()*1 != $("#phoneNumberRepresent").val() || $("#phoneNumberRepresent").val().length < 7 || $("#phoneNumberRepresent").val().length > 13) {
            $("#phoneNumberRepresentMessage").text("Số điện thoại sai định dạng");
            $("#phoneNumberRepresent").focus();
            return false;
        }
    }
    if (!re.test($("#emailRepresent").val())) {
        $("#emailRepresentMessage").text("Email không đúng định dạng");
        $("#emailRepresent").focus();
        return false;
    }

    //	Thong tin nguoi lien he
    if (format.test($("#fullNameContact").val())) {
        $("#fullNameContactMessage").text("Họ và tên không được chứa ký tự đặc biệt");
        $("#fullNameContact").focus();
        return false;
    }
    if ($("#fullNameContact").val().trim() === '') {
        $("#fullNameContactMessage").text("Họ và tên không được để trống");
        $("#fullNameContact").focus();
        return false;
    }
    if ($("#emailContact").val().trim() === '') {
        $("#emailContactMessage").text("Email không được để trống");
        $("#emailContact").focus();
        return false;
    }
    if ($("#telephoneContact").val().length > 0){
        if ($("#telephoneContact").val()*1 != $("#telephoneContact").val() || $("#telephoneContact").val().length < 7 || $("#telephoneContact").val().length > 13) {
            $("#telephoneContactMessage").text("Số điện thoại sai định dạng");
            $("#telephoneContact").focus();
            return false;
        }
    }
    if ($("#phoneNumberContact").val().length > 0){
        if ($("#phoneNumberContact").val()*1 != $("#phoneNumberContact").val() || $("#phoneNumberContact").val().length < 7 || $("#phoneNumberContact").val().length > 13) {
            $("#phoneNumberContactMessage").text("Số điện thoại sai định dạng");
            $("#phoneNumberContact").focus();
            return false;
        }
    }
    if (!re.test($("#emailContact").val())) {
        $("#emailContactMessage").text("Email không đúng định dạng");
        $("#emailContact").focus();
        return false;
    }
})
$(document).on('click','#doEdit',function(e) {
    var format = /[`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    // var vnf_regex = /((09|03|02|84|07|08|05)+([0-9]{5,11})\b)/g;
    var vnf_regex = /^[0-9]{7,13}$/g;
    var regex_ac = /^[a-zA-Z0-9]{8,10}$/;
    var business_regex = /[a-z0-9A-Z]{10}-[0-9a-zA-Z]{3}/;
    if (format.test($("#agencyName").val())) {
        $("#agencyNameMessage").text("Tên đại lý không được chứa ký tự đặc biệt");
        $("#agencyName").focus();
        return false;
    }
    if ($("#agencyName").val().trim() === '') {
        $("#agencyNameMessage").text("Bạn chưa nhập tên đại lý");
        $("#agencyName").focus();
        return false;
    }
    if ($("#businessLicense").val().trim() === '') {
        $("#businessLicenseMessage").text("Bạn chưa nhập số giấy phép kinh doanh");
        $("#businessLicense").focus();
        return false;
    }
    if (!regex_ac.test($("#businessLicense").val())){
        $("#businessLicenseMessage").text("Bạn chưa nhập đúng định dạng");
        $("#businessLicense").focus();
        return false;
    }
    if ($("#licenseDate").val().trim() === '') {
        $("#licenseDateMessage").text("Bạn chưa nhập ngày cấp phép kinh doanh");
        $("#licenseDate").focus();
        return false;
    }
    if ($("#licensePlace").val().trim() === '') {
        $("#licensePlaceMessage").text("Bạn chưa nhập nơi cấp phép kinh doanh");
        $("#licensePlace").focus();
        return false;
    }
    if ($("#taxCode").val().trim() === '') {
        $("#taxCodeMessage").text("Bạn chưa nhập mã số thuế");
        $("#taxCode").focus();
        return false;
    }
    if (!regex_ac.test($("#taxCode").val())) {
        $("#taxCodeMessage").text("Mã số thuế không đúng định dạng");
        $("#taxCode").focus();
        return false;
    }
    if ($("#phoneNumber").val().trim() === '') {
        $("#phoneNumberMessage").text("Bạn chưa nhập số điện thoại");
        $("#phoneNumber").focus();
        return false;
    }
    if (!vnf_regex.test($("#phoneNumber").val())) {
        $("#phoneNumberMessage").text("Số điện thoại sai định dạng");
        $("#phoneNumber").focus();
        return false;
    }
    if ($("#email").val().trim() === '') {
        $("#emailMessage").text("Bạn chưa nhập email");
        $("#email").focus();
        return false;
    }
    if (!re.test($("#email").val())) {
        $("#emailMessage").text("Email không đúng định dạng");
        $("#email").focus();
        return false;
    }
    //	validate thong tin lien he dai ly
    //	thong tin nguoi dai dien
    if (format.test($("#fullNameRepresent").val())) {
        $("#fullNameRepresentMessage").text("Họ và tên không được chứa ký tự đặc biệt");
        $("#fullNameRepresent").focus();
        return false;
    }
    if ($("#fullNameRepresent").val().trim() === '') {
        $("#fullNameRepresentMessage").text("Họ và tên không được để trống");
        $("#fullNameRepresent").focus();
        return false;
    }
    if ($("#emailRepresent").val().trim() === '') {
        $("#emailRepresentMessage").text("Email không được để trống");
        $("#emailRepresent").focus();
        return false;
    }
    if ($("#telephoneRepresent").val().length > 0){
        if ($("#telephoneRepresent").val()*1 != $("#telephoneRepresent").val() || $("#telephoneRepresent").val().length < 7 || $("#telephoneRepresent").val().length > 13) {
            $("#telephoneRepresentMessage").text("Số điện thoại sai định dạng");
            $("#telephoneRepresent").focus();
            return false;
        }
    }
    if ($("#phoneNumberRepresent").val().length > 0){
        if ($("#phoneNumberRepresent").val()*1 != $("#phoneNumberRepresent").val() || $("#phoneNumberRepresent").val().length < 7 || $("#phoneNumberRepresent").val().length > 13) {
            $("#phoneNumberRepresentMessage").text("Số điện thoại sai định dạng");
            $("#phoneNumberRepresent").focus();
            return false;
        }
    }
    if (!re.test($("#emailRepresent").val())) {
        $("#emailRepresentMessage").text("Email không đúng định dạng");
        $("#emailRepresent").focus();
        return false;
    }

    //	Thong tin nguoi lien he
    if (format.test($("#fullNameContact").val())) {
        $("#fullNameContactMessage").text("Họ và tên không được chứa ký tự đặc biệt");
        $("#fullNameContact").focus();
        return false;
    }
    if ($("#fullNameContact").val().trim() === '') {
        $("#fullNameContactMessage").text("Họ và tên không được để trống");
        $("#fullNameContact").focus();
        return false;
    }
    if ($("#emailContact").val().trim() === '') {
        $("#emailContactMessage").text("Email không được để trống");
        $("#emailContact").focus();
        return false;
    }
    if ($("#telephoneContact").val().length > 0){
        if ($("#telephoneContact").val()*1 != $("#telephoneContact").val() || $("#telephoneContact").val().length < 7 || $("#telephoneContact").val().length > 13) {
            $("#telephoneContactMessage").text("Số điện thoại sai định dạng");
            $("#telephoneContact").focus();
            return false;
        }
    }
    if ($("#phoneNumberContact").val().length > 0){
        if ($("#phoneNumberContact").val()*1 != $("#phoneNumberContact").val() || $("#phoneNumberContact").val().length < 7 || $("#phoneNumberContact").val().length > 13) {
            $("#phoneNumberContactMessage").text("Số điện thoại sai định dạng");
            $("#phoneNumberContact").focus();
            return false;
        }
    }
    if (!re.test($("#emailContact").val())) {
        $("#emailContactMessage").text("Email không đúng định dạng");
        $("#emailContact").focus();
        return false;
    }
})
var invalidChars = [ "-", "+", "e", ",", "." ];
$("#phoneNumber , #vasNumber, #phoneNumberContact, #telephoneContact, #telephoneRepresent, #phoneNumberRepresent").on("keydown", function(e) {
    if (invalidChars.includes(e.key)) {
        e.preventDefault();
    }
});