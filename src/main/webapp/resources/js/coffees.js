$(window).on('hashchange', routePage).trigger('hashchange');

function routePage() {
    //checking the hash of page url
    let pageName = (window.location.hash) ? window.location.hash : "#coffeeList";
    if (pageName === "#coffeeList") {
        //getting data for coffeeList page
        getCoffeListPage();
    }
    if (pageName === "#order") {
        //getting data for order page
        goToOrder();
    }
    if (pageName === "#confirmOrder") {
        //getting data for confirmOrder page
        goToConfirmOrder();
    }
    if (pageName === "#congratulation") {
        //drawing the congratulation page
        drawCongratulationPage();
    }
    if (pageName === "#admin") {
        //getting data for admin page
        goToAdminPage();
    }
    if (pageName === "#login") {
        //drawing the login page
        drawLoginPage();
    }
    if (pageName.indexOf("#orderDetails") >= 0) {
        //getting data for orderDetails page
        let array = window.location.hash.split("=");
        let id = array[array.length - 1];
        goToOrderDetailsPage(id)
    }
}

/**
 * loading data for drawing coffeList page
 */
function getCoffeListPage() {

    let coffee;
    $.ajax
    ({
        async: false,
        type: "GET",
        url: "api/v1/coffees",
        dataType: 'json',
        success: function (data) {
            console.log(data)
            coffee = data;
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
    let order;
    $.ajax
    ({
        async: false,
        type: "GET",
        url: "api/v1/order",
        dataType: 'json',
        success: function (data) {
            order = data;
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
    //drawing coffeeList page
    drawCoffeeListPage(coffee, order);
}

/**
 * adding coffee to basket
 * @param id of choosed coffee
 */
function addToBasket(id) {
    let quantity = document.getElementById(id);
    $.ajax
    ({
        type: "POST",
        url: "api/v1/order",
        data: {quantity: quantity.value, id: id},
        dataType: 'json',
        success: function (data) {
            console.log(data);
            drawCoffeeListPage(null, data)
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
}

/**
 * loading data for drawing order page
 */
function goToOrder() {
    $.ajax
    ({
        async: false,
        type: "GET",
        url: "api/v1/order",
        dataType: 'json',
        success: function (data) {
            if(data.order===null){
                location.hash="#coffeeList"
            }else{
                drawOrderPage(data)
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
}

/**
 * loading data for drawing confirmOrder page
 */
function goToConfirmOrder() {
    $.ajax
    ({
        type: "GET",
        url: "api/v1/order",
        dataType: 'json',
        success: function (data) {
            if(data.order===null){
                location.hash="#coffeeList"
            }else{
                drawConfirmOrderPage(data);
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
}

/**
 * messaging about empty order
 */
function checkOrder() {

    alert("basket is empty")
}

/**
 * deleting orderItem from order
 * @param id of coffee which will be deleted
 */
function deleteFromOrder(id) {
    let url = "api/v1/orderItem/" + id;
    $.ajax
    ({
        type: "DELETE",
        url: url,
        data: {id: id},
        dataType: 'json',
        success: function (data) {
            console.log(data)
            if(data.price===0){
                location.hash="#coffeeList"
            }
            let item = document.getElementById("item" + id);
            item.parentNode.removeChild(item);
            let totalPrice = document.getElementById("totalPrice");
            totalPrice.innerHTML = data.price;
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
}

/**
 * updating orderItems quantity
 * @param val new quantity or orderItem
 * @param id of coffee
 */
function changeOrder(val, id) {
    if (parseInt(val.value) != val.value || val.value <= 0) {
        alert("wrong quantity")
        val.value = "1"
    } else {
        $.ajax
        ({
            type: "POST",
            url: "api/v1/order/price",
            data: {quantity: val.value, id: id, method: "UPDATE"},
            dataType: 'json',
            success: function (data) {
                console.log(data);
                let totalPrice = document.getElementById("totalPrice");
                totalPrice.innerHTML = data.price;
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
        });
    }
}

/**
 * sending request to clean admin context
 */
function doLogOut() {
    $.ajax
    ({
        type: "GET",
        url: "logout",
        dataType: 'json',
        success: function (data) {
            console.log(data);
            if (data.message === "success logout") {
                location.hash = "#login";
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },

    });
}

/**
 * changing delivery status of order
 * @param but button tag which must be hide onclick
 * @param id of order
 */
function changeDeliveryStatus(but, id) {
    let url = "api/v1/admin/order/" + id;
    $.ajax
    ({
        type: "POST",
        url: url,
        data: {id: id},
        dataType: 'json',
        success: function (data) {
            console.log(data)
            but.parentNode.removeChild(but);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },

    });
}

/**
 * getting order details
 * @param id of order
 */
function goToOrderDetailsPage(id) {
    $.ajax
    ({
        type: "GET",
        url: "api/v1/admin/order/" + id,
        data: {id: id},
        dataType: 'json',
        success: function (data) {
            console.log(data)
            drawOrderDetailsPage(data);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },

    });
}

/**
 * checking of users name, address and phone
 * saving the order
 */
function checkValidData() {
    let name = document.getElementById("name");
    let address = document.getElementById("address");
    let phone = document.getElementById("phone");
    var pattName = new RegExp("^[a-zA-Zа-яА-Я'][a-zA-Zа-яА-Я-' ]+[a-zA-Zа-яА-Я']?$");
    var pattPhone = new RegExp("[\\+]375\\d{9}");
    let nameError = document.getElementById("nameError");
    let phoneError = document.getElementById("phoneError");
    let addressError = document.getElementById("addressError");
    let goToNext = true;
    if (!pattName.test(name.value)) {
        nameError.innerHTML = "It should not contain symbols other than - '";
        goToNext = false;
    } else {
        nameError.innerHTML = "";
    }
    if (!pattPhone.test(phone.value)) {
        phoneError.innerHTML = "must start +375 and 9 simbols after";
        goToNext = false;
    } else {
        phoneError.innerHTML = "";
    }
    if (address.value.trim().length == "0") {
        addressError.innerHTML = "should not be empty or contains only spaces";
        goToNext = false;
        console.log("ERRRR")
    } else {
        addressError.innerHTML = "";
    }

    if (goToNext) {
        $.ajax
        ({
            type: "PUT",
            url: "api/v1/order",
            data: {name: name.value, phone: phone.value, address: address.value},
            dataType: 'json',
            success: function (data) {
                console.log(data);
                location.hash = "#congratulation"
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
        });
    }

}

/**
 * sending admin login and password to try authorize
 */
function tryToAuthorize() {
    let username = document.getElementById("userEmail");
    let password = document.getElementById("password");
    $.ajax
    ({
        type: "POST",
        url: "login",
        data: {username: username.value, password: password.value},
        dataType: 'json',
        success: function (data) {
        console.log("DDDDDDDDD")
            console.log(data)
            if (data.message === "success") {
                location.hash = "#admin"
            } else {
                let feedback = document.getElementById("feedback");
                feedback.innerHTML = data.message;
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },

    });
}

/**
 * getting data for admin page
 */
function goToAdminPage() {
    $.ajax
    ({
        type: "GET",
        url: "api/v1/admin/orders",
        dataType: 'json',
        success: function (data) {
            console.log("EEEEEEEEE")
            console.log(data.message)
            if (data.message === "access denied") {
                location.hash = "#login";
            } else {
                drawAdminPage(data);
            }

        },
        error: function (e) {
            console.log("ERROR: ", e);
        },

    });
}

/**
 * drawing coffeeList page
 * @param coffee object with fields which should be draw
 * @param order object with fields which should be draw
 */
function drawCoffeeListPage(coffee, order) {
    console.log(coffee)
    $('.pages').hide();
    let s;
    if (order.order) {

        s = '<a href="#order" class=\"btn btn-info btn-lg basket\">' +
            '<span class="glyphicon glyphicon-shopping-cart"></span>&nbsp;' +
            '<span>' + order.totalQuantity + '</span> <br>';
        for (let i = 0; i < order.order.orderItems.length; i++) {
            s = s + '<span style="float: left">' + order.order.orderItems[i].coffee.name + '&nbsp;</span> <span style="float: right;">' + order.order.orderItems[i].quantity + '</span><br>';
        }
        s = s + '<span style="color:red">Total price:&nbsp;</span><span style="color:red">' + order.order.price + '</span></a>';

    } else {
        s = '<a onclick="checkOrder()" class=\"btn btn-info btn-lg basket\">' +
            '<span class="\glyphicon glyphicon-shopping-cart\"></span>' +
            '<span>' + '&nbsp;0' + '</span> <br></a>';
    }
    let basket = document.getElementById("basket")
    basket.innerHTML = s;
    if (coffee) {
        s = "";
        s = s + '<div class="container">' +
            '<table class="table table-hover">' +
            '<thead>' +
            '<tr>' +
            '<th>Name</th>' +
            '<th>Description</th>' +
            '<th>Price</th>' +
            '<th>Quantity</th>' +
            '<th>Add</th>' +
            '</tr>' +
            '</thead>' +
            '<tbody>';
        for (let i = 0; i < coffee.length; i++) {
            s = s + '<tr>' +
                '<td>' + coffee[i].name + '</td>' +
                '<td>' + coffee[i].description + '</td>' +
                '<td>' + coffee[i].price + '</td>' +
                '<td style="width: 200px"><input type="number" id="' + coffee[i].id + '" name="quantity" value="1" min="1"></td>' +
                '<td style="min-width: 100px">' +
                '<button onclick="addToBasket(' + coffee[i].id + ')" type="button" value="" class="btn btn-info btn-lg">Add</button>' +
                '</td>' +
                '</tr>';
        }
        s = s + '</tbody>' +
            '</table>' +
            '</div>';
        let coffees = document.getElementById("coffees")
        coffees.innerHTML = s;
    }
    let coffeeList = document.getElementById("coffeeList")
    $(coffeeList).show();
}

/**
 * drawing order page
 * @param data order object with fields which should be draw
 */
function drawOrderPage(data) {
    console.log(data)
    $('.pages').hide();
    let s = '<a href=\"#coffeeList\" class=\"btn btn-info btn-lg basket\">Add Other Coffee</a>';
    s = s + '<div class="container">' +
        '<table class="table table-hover">' +
        '<thead>' +
        '<tr>' +
        '<th>Name</th>' +
        '<th>Description</th>' +
        '<th>Price</th>' +
        '<th>Quantity</th>' +
        '<th>Remove</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>';
    for (let i = 0; i < data.order.orderItems.length; i++) {
        s = s + '<tr id="item' + data.order.orderItems[i].coffee.id + '">' +
            '<td>' + data.order.orderItems[i].coffee.name + '</td>' +
            '<td>' + data.order.orderItems[i].coffee.description + '</td>' +
            '<td>' + data.order.orderItems[i].coffee.price + '</td>' +
            '<td style="width: 200px"><input onchange="changeOrder(this,' + data.order.orderItems[i].coffee.id + ')" type="number" name="quantity" value=\"' + data.order.orderItems[i].quantity + '\" min="1"></td>' +
            '<td style="min-width: 100px">' +
            '<button onclick="deleteFromOrder(' + data.order.orderItems[i].coffee.id + ')" type="button" value="" class="btn btn-info btn-lg">Remove</button>' +
            '</td>' +
            '</tr>';
    }
    s = s + '<tr>' +
        '<td colspan="4" style="text-align:right"> Total price</td>' +
        '<td colspan="2" id="totalPrice">' + data.order.price + '</td>' +
        '</tr>' +
        '</tbody>' +
        '</table>' +
        '<a href="#confirmOrder"><button class="btn btn-info btn-lg">Order</button></a>' +
        '</div>';
    let order = document.getElementById("order")
    order.innerHTML = s;
    $(order).show();
}

/**
 * drawing confirm order page
 * @param data order object with fields which should be draw
 */
function drawConfirmOrderPage(data) {
    $('.pages').hide();
    let s = '<a href=\"#order\" class=\"btn btn-info btn-lg basket\">Change order</a>';
    s = s + '<div class="container">' +
        '<table class="table table-hover">' +
        '<thead>' +
        '<tr>' +
        '<th>Name</th>' +
        '<th>Description</th>' +
        '<th>Quantity</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>';
    for (let i = 0; i < data.order.orderItems.length; i++) {
        s = s + '<tr>' +
            '<td>' + data.order.orderItems[i].coffee.name + '</td>' +
            '<td>' + data.order.orderItems[i].coffee.description + '</td>' +
            '<td>' + data.order.orderItems[i].quantity + '</td>' +
            '</tr>';
    }
    s = s + '</tbody>' +
        '</table>' +
        '<label for="name">Name</label>&nbsp;<span id="nameError" style="color: red;"></span>' +
        '<input type="text" class="form-control" id="name" name="name" placeholder="name" max="50" pattern="^[a-zA-Zа-яА-Я\'][a-zA-Zа-яА-Я-\' ]+[a-zA-Zа-яА-Я\']?$" title="It should not contain symbols other than - \'" required><br>' +
        '<label for="address">Address</label>&nbsp;<span id="addressError" style="color: red;"></span>' +
        '<input type="text" class="form-control" id="address" name="address" min="5" placeholder="address" max="100" required title="should not be empty or contains only spaces"><br>' +
        '<label for="phone">Phone number</label>&nbsp;<span id="phoneError" style="color: red;"></span>' +
        '<input type="text" class="form-control" id="phone" name="phone" pattern="[\\+]375\\d{9}" placeholder="+375296522540" title="must start +375 and 9 simbols after" required><br>' +
        '<a onclick="checkValidData()" "  class="btn btn-info btn-lg">Confirm order</a>' +
        '</div>';
    let confirmOrder = document.getElementById("confirmOrder");
    confirmOrder.innerHTML = s;
    $(confirmOrder).show();
}

/**
 * drawing congratulation page
 */
function drawCongratulationPage() {
    $('.pages').hide();
    let s = '<div class="congrat"><p>Thanks for your order!</p>' +
        '<a href="#coffeeList">Chose more coffee</a></div>';
    let congratulation = document.getElementById("congratulation");
    congratulation.innerHTML = s;
    $(congratulation).show();
}


/**
 * drawing login page
 */
function drawLoginPage() {
    $('.pages').hide();
    let s = '<div id="feedback"></div>' +
        '<div  class="container-fluid">' +
        '<div class="row center-block">' +
        '<div class="col-md-5"></div>' +
        '<div class="col-md-2">' +
        '<div class="form-group">' +
        '<label for="userEmail">Log in</label>' +
        '<input type="text" class="form-control" id="userEmail" name="username" placeholder="Login">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="password">Password</label>' +
        '<input type="password" class="form-control" id="password" name="password" placeholder="Password">' +
        '</div>' +
        '<button onclick="tryToAuthorize()" class="btn btn-info btn-lg">Log in</button>' +
        '</div>' +
        '<div class="col-md-5"></div>' +
        '</div>' +
        '</div>';
    let login = document.getElementById("login");
    login.innerHTML = s;
    $(login).show();
}


/**
 * drawing admin page
 * @param data list of order objects with fields which should be draw
 */
function drawAdminPage(data) {
    console.log(data)
    $('.pages').hide();
    let s;
    if(data.length===0){
        s='<a onclick="doLogOut()" class=\"btn btn-info btn-lg basket\">Log out</a><div class="congrat"><p>No Orders Yet!</p></div>';
    }else{
        s= '<a onclick="doLogOut()" class=\"btn btn-info btn-lg basket\">Log out</a>';
        s = s + '<div class="container">' +
            '<table class="table table-hover">' +
            '<thead>' +
            '<tr>' +
            '<th>Address</th>' +
            '<th>Name</th>' +
            '<th>Phone</th>' +
            '<th>Price</th>' +
            '<th>Delivery Status</th>' +
            '<th>Details</th>' +
            '</tr>' +
            '</thead>' +
            '<tbody>';
        for (let i = 0; i < data.length; i++) {
            s = s + '<tr>' +
                '<td>' + data[i].address + '</td>' +
                '<td>' + data[i].userName + '</td>' +
                '<td>' + data[i].phoneNumber + '</td>' +
                '<td>' + data[i].price + '</td>';
            if (data[i].delivery == "0") {
                s = s + '<td style="min-width: 100px"><button onclick="changeDeliveryStatus(this,' + data[i].id + ')" type="button" class="btn btn-info btn-lg">Delivered</button></td>';
            } else {
                s = s + '<td style="min-width: 100px"></td>';
            }
            s = s + '<td ><a href="#orderDetails$id=' + data[i].id + '" class="btn btn-info btn-lg">Show details</a></td>'
        }
        s = s + '</tbody></table></div>';
    }

    let admin = document.getElementById("admin");
    admin.innerHTML = s;
    $(admin).show();
}


/**
 * drawing order details page
 * @param data order object with fields which should be draw
 */
function drawOrderDetailsPage(data) {
    $('.pages').hide();
    let s = '<a href="#admin" class=\"btn btn-info btn-lg basket\">All Orders</a>';
    s = s + '<div class="container">' +
        '<table class="table table-hover">' +
        '<thead>' +
        '<tr>' +
        '<th>Coffee Name</th>' +
        '<th>Price</th>' +
        '<th>Quantity</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>';
    for (let i = 0; i < data.orderItems.length; i++) {
        s = s + '<tr>' +
            '<td>' + data.orderItems[i].coffee.name + '</td>' +
            '<td>' + data.orderItems[i].coffee.price + '</td>' +
            '<td>' + data.orderItems[i].quantity + '</td></tr>';

    }
    s = s + '<tr>' +
        '<td colspan="2" style="text-align:right"> Total price</td>' +
        '<td >' + data.price + '</td>' +
        '</tr>'
    s = s + '</tbody></table></div>';
    let orderDetails = document.getElementById("orderDetails");
    orderDetails.innerHTML = s;
    $(orderDetails).show();
}