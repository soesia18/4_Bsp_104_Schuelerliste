function loadStudent() {
    var element = document.getElementById("students");
    var student = element.options[element.selectedIndex];

    fetch('./StudentServlet?student=' + student.value)
        .then(
            function (response) {
                if (response.status != 200){
                    console.log('Looks like there was a problem. Status Code: ' + response.status);
                    return;
                }

                response.json().then(function (student){
                    console.log(student);
                    document.getElementById("name").innerText = student.lastName + " " + student.firstName;
                    document.getElementById("catnr").innerText = student.catNr;
                    document.getElementById("class").innerText = student.className;
                    document.getElementById("sex").innerText = student.sex;
                    document.getElementById("birthdate").innerText =
                        student.birthdate.dayOfMonth + "." + student.birthdate.monthValue + "." + student.birthdate.year;
                });

            }
        )
        .catch(function (err){
            console.log('Fetch Error :-S', err);
        });
}