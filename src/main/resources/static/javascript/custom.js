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
