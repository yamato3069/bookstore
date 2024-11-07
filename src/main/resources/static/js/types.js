/**
 * 
 */
document.getElementById('payment-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const paymentType = document.querySelector('input[name="payment-method"]:checked').value;
    const formData = new FormData();

    // 各支払い方法で共通の情報
    formData.append('bookId', document.getElementById('book-id').value);
    formData.append('quantity', document.getElementById('quantity').value);
    formData.append('amount', document.getElementById('amount').value);
    formData.append('paymentType', paymentType);

    // 各支払方法ごとに追加の情報を設定
    if (paymentType === 'credit-card') {
        formData.append('cardNumber', document.getElementById('card-number').value);
        formData.append('cardHolder', document.getElementById('card-holder').value);
    } else if (paymentType === 'paypal') {
        formData.append('email', document.getElementById('paypal-email').value);
    }
    // 銀行振込や代引きの場合は特に追加情報を必要としない場合、そのままPOSTします

    // AJAXを使ってフォームを送信
    fetch('/purchase', {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        if (data === 'success') {
            alert('購入が完了しました！');
            window.location.href = '/confirmation'; // 完了画面へリダイレクト
        } else {
            alert('購入に失敗しました。もう一度お試しください。');
        }
    })
    .catch(error => {
        console.error('エラーが発生しました:', error);
        alert('エラーが発生しました。');
    });
});
