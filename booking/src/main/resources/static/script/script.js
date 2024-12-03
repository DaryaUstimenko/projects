function setMark(value) {
           document.getElementById("markInput").value = value;
           document.querySelector("form").action = "/api/v1/hotel/rating(mark=" + value + ")";
       }


fetch('/api/v1/booking/unavailable-dates')

    .then(response => response.json())

    .then(dates => {

        $('#startDate, #endDate').datepicker({

            datesDisabled: dates,

            startDate: new Date(),

            endDate: "2025-12-31",

            format: "yyyy-mm-dd",

            language: "ru",

            autoclose: true

        });

    });
