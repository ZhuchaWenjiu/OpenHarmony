/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 3);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */,
/* 1 */,
/* 2 */,
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(4)
var $app_style$ = __webpack_require__(5)
var $app_script$ = __webpack_require__(6)

$app_define$('@app-component/index', [], function($app_require$, $app_exports$, $app_module$) {

$app_script$($app_module$, $app_exports$, $app_require$)
if ($app_exports$.__esModule && $app_exports$.default) {
$app_module$.exports = $app_exports$.default
}

$app_module$.exports.template = $app_template$

$app_module$.exports.style = $app_style$

})
$app_bootstrap$('@app-component/index',undefined,undefined)

/***/ }),
/* 4 */
/***/ (function(module, exports) {

module.exports = {
  "type": "div",
  "attr": {},
  "classList": [
    "container"
  ],
  "children": [
    {
      "type": "div",
      "attr": {},
      "classList": [
        "title-section"
      ],
      "children": [
        {
          "type": "div",
          "attr": {},
          "classList": [
            "title"
          ],
          "children": [
            {
              "type": "text",
              "attr": {
                "value": "Food"
              },
              "classList": [
                "name"
              ]
            },
            {
              "type": "text",
              "attr": {
                "value": "Choose What You Like"
              },
              "classList": [
                "sub-title"
              ]
            }
          ]
        }
      ]
    },
    {
      "type": "div",
      "attr": {},
      "children": [
        {
          "type": "swiper",
          "attr": {
            "id": "swiperImage"
          },
          "id": "swiperImage",
          "classList": [
            "swiper-style"
          ],
          "children": [
            {
              "type": "image",
              "attr": {
                "src": function () {return this.$item},
                "focusable": "true"
              },
              "classList": [
                "image-mode"
              ],
              "repeat": function () {return this.imageList}
            }
          ]
        },
        {
          "type": "div",
          "attr": {},
          "classList": [
            "container"
          ],
          "children": [
            {
              "type": "div",
              "attr": {},
              "classList": [
                "description-first-paragraph"
              ],
              "children": [
                {
                  "type": "text",
                  "attr": {
                    "value": function () {return this.descriptionFirstParagraph}
                  },
                  "classList": [
                    "description"
                  ]
                }
              ]
            },
            {
              "type": "div",
              "attr": {},
              "classList": [
                "color-column"
              ],
              "children": [
                {
                  "type": "canvas",
                  "attr": {
                    "id": function () {return this.$item.id},
                    "focusable": "true"
                  },
                  "id": function () {return this.$item.id},
                  "events": {
                    "focus": function (evt) {this.swipeToIndex(this.$item.index,evt)}
                  },
                  "classList": [
                    "color-item"
                  ],
                  "repeat": function () {return this.canvasList}
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "type": "div",
      "attr": {},
      "classList": [
        "cart"
      ],
      "children": [
        {
          "type": "text",
          "attr": {
            "focusable": "true",
            "value": function () {return this.cartText}
          },
          "classList": function () {return [this.cartStyle]},
          "events": {
            "click": "addCart",
            "focus": "getFocus",
            "blur": "lostFocus"
          }
        }
      ]
    }
  ]
}

/***/ }),
/* 5 */
/***/ (function(module, exports) {

module.exports = {
  ".container": {
    "flexDirection": "column"
  },
  ".title-section": {
    "flexDirection": "row",
    "height": "60px",
    "marginBottom": "5px",
    "marginTop": "10px"
  },
  ".title": {
    "alignItems": "flex-start",
    "flexDirection": "column",
    "paddingLeft": "60px",
    "paddingRight": "160px"
  },
  ".name": {
    "fontSize": "20px"
  },
  ".sub-title": {
    "fontSize": "15px",
    "color": "#7a787d",
    "marginTop": "10px"
  },
  ".swiper-style": {
    "height": "250px",
    "width": "350px",
    "indicatorColor": "#4682b4",
    "indicatorSelectedColor": "#f0e68c",
    "indicatorSize": "10px",
    "marginLeft": "50px"
  },
  ".image-mode": {
    "objectFit": "contain"
  },
  ".color-column": {
    "flexDirection": "row",
    "alignContent": "center",
    "marginTop": "20px"
  },
  ".color-item": {
    "height": "50px",
    "width": "50px",
    "marginLeft": "50px",
    "paddingLeft": "10px",
    "backgroundColor:focus": "#FFFFFF"
  },
  ".description-first-paragraph": {
    "paddingLeft": "60px",
    "paddingRight": "60px",
    "paddingTop": "30px"
  },
  ".description": {
    "color": "#7a787d",
    "fontSize": "15px"
  },
  ".cart": {
    "justifyContent": "center",
    "marginTop": "20px"
  },
  ".cart-text": {
    "fontSize": "20px",
    "textAlign": "center",
    "width": "300px",
    "height": "50px",
    "backgroundColor": "#6495ed",
    "color": "#FFFFFF"
  },
  ".cart-text-focus": {
    "fontSize": "20px",
    "textAlign": "center",
    "width": "300px",
    "height": "50px",
    "backgroundColor": "#4169e1",
    "color": "#FFFFFF"
  },
  ".add-cart-text": {
    "fontSize": "20px",
    "textAlign": "center",
    "width": "300px",
    "height": "50px",
    "backgroundColor": "#ffd700",
    "color": "#FFFFFF"
  }
}

/***/ }),
/* 6 */
/***/ (function(module, exports) {

module.exports = function(module, exports, $app_require$){"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _default = {
  data: {
    cartText: 'Add To Cart',
    cartStyle: 'cart-text',
    isCartEmpty: true,
    descriptionFirstParagraph: 'This is the food page including fresh fruit, meat, snack and etc. You can pick whatever you like and add it to your Cart. Your order will arrive within 48 hours. We gurantee that our food is organic and healthy. Feel free to ask our 24h online service to explore more about our platform and products.',
    imageList: ['/common/ii1.jpg', '/common/ii2.jpg', '/common/ii3.jpg', '/common/ii4.jpg'],
    canvasList: [{
      id: 'cycle0',
      index: 0,
      color: '#f0b400'
    }, {
      id: 'cycle1',
      index: 1,
      color: '#e86063'
    }, {
      id: 'cycle2',
      index: 2,
      color: '#597a43'
    }, {
      id: 'cycle3',
      index: 3,
      color: '#e97d4c'
    }]
  },
  onShow: function onShow() {
    var _this = this;

    this.canvasList.forEach(function (element) {
      _this.drawCycle(element.id, element.color);
    });
  },
  swipeToIndex: function swipeToIndex(index) {
    this.$element('swiperImage').swipeTo({
      index: index
    });
  },
  drawCycle: function drawCycle(id, color) {
    var greenCycle = this.$element(id);
    var ctx = greenCycle.getContext("2d");
    ctx.strokeStyle = color;
    ctx.fillStyle = color;
    ctx.beginPath();
    ctx.arc(15, 25, 10, 0, 2 * 3.14);
    ctx.closePath();
    ctx.stroke();
    ctx.fill();
  },
  addCart: function addCart() {
    if (this.isCartEmpty) {
      this.cartText = 'Cart + 1';
      this.cartStyle = 'add-cart-text';
      this.isCartEmpty = false;
    }
  },
  getFocus: function getFocus() {
    if (this.isCartEmpty) {
      this.cartStyle = 'cart-text-focus';
    }
  },
  lostFocus: function lostFocus() {
    if (this.isCartEmpty) {
      this.cartStyle = 'cart-text';
    }
  }
};
exports["default"] = _default;
var moduleOwn = exports.default || module.exports;
var accessors = ['public', 'protected', 'private'];
if (moduleOwn.data && accessors.some(function (acc) {
    return moduleOwn[acc];
  })) {
  throw new Error('For VM objects, attribute data must not coexist with public, protected, or private. Please replace data with public.');
} else if (!moduleOwn.data) {
  moduleOwn.data = {};
  moduleOwn._descriptor = {};
  accessors.forEach(function(acc) {
    var accType = typeof moduleOwn[acc];
    if (accType === 'object') {
      moduleOwn.data = Object.assign(moduleOwn.data, moduleOwn[acc]);
      for (var name in moduleOwn[acc]) {
        moduleOwn._descriptor[name] = {access : acc};
      }
    } else if (accType === 'function') {
      console.warn('For VM objects, attribute ' + acc + ' value must not be a function. Change the value to an object.');
    }
  });
}}
/* generated by ace-loader */


/***/ })
/******/ ]);
//# sourceMappingURL=index.js.map