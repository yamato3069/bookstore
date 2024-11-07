/**
 * 
 */
document.getElementById('payment-form').addEventListener('submit', function(event) {
			event.preventDefault();

			const selectedMethod = document.querySelector('input[name="payment-method"]:checked').value;

			let url;
			switch (selectedMethod) {
				case 'credit-card':
					url = '/payment/creditCardSettings'; // クレジットカード設定画面
					break;
				case 'paypal':
					url = '/payment/paypalSettings'; // PayPal設定画面
					break;
				case 'bank-transfer':
					url = '/payment/bankTransferSettings'; // 銀行振込設定画面
					break;
				case 'cash-on-delivery':
					url = '/payment/cashOnDeliverySettings'; // 代引き設定画面
					break;
				default:
					alert('支払い方法を選択してください');
					return;
			}

			window.location.href = url;
		});
