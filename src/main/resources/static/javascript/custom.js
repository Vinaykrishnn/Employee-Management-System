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
