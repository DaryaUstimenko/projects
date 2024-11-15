function setMark(value) {
           document.getElementById("markInput").value = value;
           document.querySelector("form").action = "/api/v1/hotel/rating(mark=" + value + ")";
       }



