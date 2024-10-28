const accordion = document.querySelector(".accordion");
const panel = document.querySelector(".panel");

accordion.addEventListener("click", function() {
    this.classList.toggle("active");
    if (panel.style.display === "block") {
        panel.style.display = "none";
    } else {
        panel.style.display = "block";
    }
});

const added_items = {};
const cartCountElement = document.getElementById("cart-count");

function updateCartCount() {
    let totalCount = 0;
    for (const item in added_items) {
        totalCount += added_items[item].quantity;
    }
    if (totalCount > 0) {
        cartCountElement.textContent = totalCount;
        cartCountElement.style.display = 'block';
    } else {
        cartCountElement.style.display = 'none';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    fetch('/bookSearch/getCart')
        .then(response => response.json())
        .then(cart => {
            for (const bookId in cart) {
                added_items[bookId] = {
                    price: cart[bookId].price,
                    quantity: cart[bookId].quantity
                };
            }
            updateCartCount();
        })
        .catch(error => console.error('Error fetching cart:', error));
});

document.querySelectorAll('.add-to-cart').forEach(button => {
    button.addEventListener('click', () => {
        const bookId = button.getAttribute('data-book-id');
        const price = button.getAttribute('data-book-price');
        const title = button.getAttribute('data-book-title');
        const authorName = button.getAttribute('data-book-authorName');
        const imagePath = button.getAttribute('data-book-imagePath');
        const stock = parseInt(button.getAttribute('data-book-stock'), 10);
        const currentQuantity = added_items[bookId] ? added_items[bookId].quantity : 0;

        if (currentQuantity < stock) {
            const newQuantity = currentQuantity + 1;

            fetch('/bookSearch/addToCart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    'imagePath': imagePath,
                    'bookId': bookId,
                    'price': price,
                    'title': title,
                    'authorName': authorName,
                    'quantity': newQuantity
                })
            })
            .then(response => {
                if (response.ok) {
                    console.log('カートに追加しました');
                    added_items[bookId] = { price: price, quantity: newQuantity }; 
                    updateCartCount();
                } else {
                    console.error('カートへの追加に失敗しました');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });

        } else {
            alert(`在庫が不足しています。${stock} 冊までしか追加できません。`);
        }
    });
});
cartCountElement.style.display = 'none';
