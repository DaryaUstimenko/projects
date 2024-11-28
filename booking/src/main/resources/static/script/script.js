function setMark(value) {
           document.getElementById("markInput").value = value;
           document.querySelector("form").action = "/api/v1/hotel/rating(mark=" + value + ")";
       }
/*<![CDATA[*/
var unavailableDates = /*[[${unavailableDates}]]*/ [];
console.log(unavailableDates);
$(function() {
  $('#startDate').datepicker({
    startDate: "2024-11-28",
    endDate: "2025-12-31",
    format: "yyyy-mm-dd",
    language: "ru",
    autoclose: true,
    datesDisabled: unavailableDates
  });
  $('#endDate').datepicker({
    startDate: "2024-11-28",
    endDate: "2025-12-31",
    format: "yyyy-mm-dd",
    language: "ru",
    autoclose: true,
    datesDisabled: unavailableDates
  });
});
/*]]>*/


