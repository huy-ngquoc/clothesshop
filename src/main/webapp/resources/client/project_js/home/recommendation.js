function previewFile() {
        const preview = document.getElementById('imagePreview');
        const file = document.getElementById('outfitImage').files[0];
        const reader = new FileReader();

        
        if (file) {
            reader.readAsDataURL(file); 
        }

        
        reader.onloadend = function () {
           
            preview.src = reader.result;
            preview.style.display = "block"; 
        }

        
        reader.onerror = function () {
            preview.src = "";
            preview.style.display = "none";
        }
    }
document.getElementById("outfitImage").addEventListener("change",(e)=>{
    console.log("Change");
    previewFile();
})