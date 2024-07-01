function checkPassword() {
    let password = document.getElementById("password").value;
    let cnfrmPassword = document.getElementById("cnfrm-password").value;
    console.log(" Password:", password, '\n', "Confirm Password:", cnfrmPassword);
    let message = document.getElementById("message");

    if (password.length != 0) {
        if (password == cnfrmPassword) {
            message.textContent = "Passwords match";
            message.style.backgroundColor = "#1dcd59";
        }
        else {
            message.textContent = "Password don't match";
            message.style.backgroundColor = "#ff4d4d";
        }
    }

}

let parameters = {
    count: false,
    letters: false,
    numbers: false,
    special: false
}
let strengthBar = document.getElementById("strength-bar");
let msg = document.getElementById("msg");

function strengthChecker() {
    let password = document.getElementById("password").value;

    parameters.letters = (/[A-Za-z]+/.test(password)) ? true : false;
    parameters.numbers = (/[0-9]+/.test(password)) ? true : false;
    parameters.special = (/[!\"$%&/()=?@~`\\.\';:+=^*_-]+/.test(password)) ? true : false;
    parameters.count = (password.length > 7) ? true : false;

    let barLength = Object.values(parameters).filter(value => value);

    console.log(Object.values(parameters), barLength);

    strengthBar.innerHTML = "";
    for (let i in barLength) {
        let span = document.createElement("span");
        span.classList.add("strength");
        strengthBar.appendChild(span);
    }

    let spanRef = document.getElementsByClassName("strength");
    for (let i = 0; i < spanRef.length; i++) {
        switch (spanRef.length - 1) {
            case 0:
                spanRef[i].style.background = "#ff3e36";
                msg.textContent = "Your password is very weak";
                break;
            case 1:
                spanRef[i].style.background = "#ff691f";
                msg.textContent = "Your password is weak";
                break;
            case 2:
                spanRef[i].style.background = "#ffda36";
                msg.textContent = "Your password is good";
                break;
            case 3:
                spanRef[i].style.background = "#0be881";
                msg.textContent = "Your password is strong";
                break;
        }
    }
}


function toggle() {
    let password = document.getElementById("password");
    let eye = document.getElementById("toggle");

    if (password.getAttribute("type") == "password") {
        password.setAttribute("type", "text");
        eye.style.color = "#0be881";
    }
    else {
        password.setAttribute("type", "password");
        eye.style.color = "#808080";
    }
}

function previewImage(event) {
    let fileInput = event.target;
    let preview = document.getElementById("preview");
  
    if (fileInput.files && fileInput.files[0]) {
      let reader = new FileReader();
  
      reader.onload = function(e) {
        let img = new Image();
        img.src = e.target.result;
  
        img.onload = function() {
          let canvas = document.createElement("canvas");
          let ctx = canvas.getContext("2d");
  
          // Imposta la dimensione massima desiderata dell'anteprima
          let maxWidth = 200;
          let maxHeight = 200;
  
          // Calcola le nuove dimensioni dell'immagine
          let width = img.width;
          let height = img.height;
          if (width > height) {
            if (width > maxWidth) {
              height *= maxWidth / width;
              width = maxWidth;
            }
          } else {
            if (height > maxHeight) {
              width *= maxHeight / height;
              height = maxHeight;
            }
          }
  
          // Ridimensiona l'immagine utilizzando il canvas
          canvas.width = width;
          canvas.height = height;
          ctx.drawImage(img, 0, 0, width, height);
  
          // Imposta l'anteprima con l'immagine ridimensionata
          preview.src = canvas.toDataURL("image/jpeg");
        };
      };
  
      reader.readAsDataURL(fileInput.files[0]);
    }
  }
  