console.log("This is not script file");

typeof toggleSidebar;

console.log(typeof $ === "function" ? "jQuery is loaded" : "jQuery is not loaded");
console.log("Vinay Saab The Great");
function toggleSidebar() {
    if ($(".sidebar").width() > 0) {
        // Close the sidebar
        $(".sidebar").animate({ width: "0" }, 300);
        $(".content").animate({ marginLeft: "0%" }, 300);
    } else {
        // Open the sidebar
        $(".sidebar").animate({ width: "18%" }, 300);
        $(".content").animate({ marginLeft: "20%" }, 300);
    }
}

const search = () => {
    let query = $("#search-input").val();
    console.log(query);

    if (query === '') {
        $(".search-result").hide();
    } else {
        console.log(query);
        // Sending request to server
        let url = `http://localhost:9000/search/${query}`;
        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {
            console.log(data);
            let text = '<div class="list-group">';
            data.forEach((contact) => {
                text += `<a href="/user/contact/${contact.contactId}" class="list-group-item list-group-item-action">${contact.name}</a>`;
            });
            text += '</div>';

            $(".search-result").html(text);
            $(".search-result").show();
        });
    }
};

//first request to server to create request
const paymentStart = () => {
    let amount = 49;

    $.ajax({
        url: '/user/create_order',
        data: JSON.stringify({ amount: amount, info: 'order_request' }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            if (response.status === "created" || response.id) {
                let options = {
                    key: "rzp_test_FPqEVMGqTcYdb5", // your Razorpay key
                    amount: response.amount,       // in paisa
                    currency: "INR",
                    name: "Smart Contact Manager",
                    description: "Premium Membership",
                    image: "", // optional logo
                    order_id: response.id,

                    // ✅ YOUR HANDLER HERE
                    handler: function (response) {
                        alert("Congrats! Payment Successful");

                        // Optional: log payment IDs
                        console.log("Payment ID:", response.razorpay_payment_id);
                        console.log("Order ID:", response.razorpay_order_id);

                        // AJAX to update user premium status
                        $.ajax({
                            url: '/user/update-premium-status',
                            type: 'POST',
                            success: function (res) {
                                alert("✅ Premium activated! Please reload or click Add Contact again.");
                            },
                            error: function (err) {
                                alert("❌ Error activating premium on server.");
                            }
                        });
                    },

                    prefill: {
                        name: "", email: "", contact: ""
                    },
                    theme: {
                        color: "#3399cc"
                    }
                };

                var rzp1 = new Razorpay(options);
                rzp1.open();
            }
        },
        error: function (error) {
            console.log(error);
            alert("❌ Something went wrong while creating the order");
        }
    });
};
