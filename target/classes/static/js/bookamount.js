/**
 * 
 */
const items = document.querySelectorAll('.item');
let totalAmount = 0;
items.forEach(item => {
	const price = parseFloat(item.querySelector('[id="price"]').textContent.replace('円', '').replace(/,/g, ''));
	const quantity = parseInt(item.querySelector('[id="quantity"]').textContent, 10);
	totalAmount += price * quantity;
});

document.getElementById('total-amount').textContent = totalAmount.toLocaleString() + '円';
