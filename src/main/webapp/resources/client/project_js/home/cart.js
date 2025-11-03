const cartTable = document.getElementById("cartBody");
const listChild = cartTable.children;
for(let child of listChild) {
     const cartTable = document.getElementById("cartBody");
    const listChild = cartTable.children;

    for (let child of listChild) {
        
        const tdQty = child.children[2]; 
        const divGroup = tdQty.querySelector(".qty-input");
        const inputForm = divGroup.querySelector("form#changeForm"); 
        const input = inputForm.querySelector("input"); 

        const ids = input.id.split("_"); 

        
        input.addEventListener("change", function () {
            const amount = input.value; 
            const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

            fetch(`/update_cart?userId=${ids[0]}&productVariantId=${ids[1]}&amount=${amount}`, {
  method: 'POST',
  headers: {
    [header]: token
  }
});
        });
    }
}