/**
 * 
 */

$(function(){
	
	/*User Register Validation*/
	
	var $userRegister = $("#userRegister")
	
	$userRegister.validate({
		rules:{
			name:{
				required:true,
				lettersonly:true
			},
			email: {
				required: true,
				space: true,
				email: true
			},
			mobileNumber: {
				required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12

			},
			password: {
				required: true,
				space: true

			},
			confirmpassword: {
				required: true,
				space: true,
				equalTo: '#password'

			},
			address: {
				required: true,
				all: true

			},

			city: {
				required: true,
				space: true

			},
			state: {
				required: true,


			},
			pincode: {
				required: true,
				space: true,
				numericOnly: true

			}, img: {
				required: true,
			}
		},
		messages:{
			name:{
				required:'name required',
				lettersonly:'invalid name'
			},
			email: {
				required: 'email id must be required',
				space: 'space not allowed',
				email: 'Invalid email'
			},
			mobileNumber: {
				required: 'mobile number must be required',
				space: 'space not allowed',
				numericOnly: 'invalid mob no',
				minlength: 'min 10 digit',
				maxlength: 'max 12 digit'
			},

			password: {
				required: 'password must be required',
				space: 'space not allowed'

			},
			confirmpassword: {
				required: 'confirm password must be required',
				space: 'space not allowed',
				equalTo: 'password mismatch'

			},
			address: {
				required: 'address must be required',
				all: 'invalid'

			},

			city: {
				required: 'city must be required',
				space: 'space not allowed'

			},
			state: {
				required: 'state must be required',
				space: 'space not allowed'

			},
			pincode: {
				required: 'pincode must be required',
				space: 'space not allowed',
				numericOnly: 'invalid pincode'

			},
			img: {
				required: 'image required',
			}
		}
	})
	
	// Reset Password Validation
	
	var $resetPassword=$("#resetPassword");
	
	$resetPassword.validate({
			
			rules:{
				password: {
					required: true,
					space: true
	
				},
				confirmpassword: {
					required: true,
					space: true,
					equalTo: '#password'
	
				}
			},
			messages:{
			   password: {
					required: 'password must be required',
					space: 'space not allowed'
	
				},
				confirmpassword: {
					required: 'confirm password must be required',
					space: 'space not allowed',
					equalTo: 'password mismatch'
	
				}
			}	
	})
	
	
	// Update Personal Information Validation
	
	var $personalInfo = $("#personalInfo");

	$personalInfo.validate({
	    rules: {
	        name: {
	            required: true,
	            lettersonly: true
	        },
	        email: {
	            required: true,
	            space: true,
	            email: true
	        },
	        mobileNumber: {
	            required: true,
	            space: true,
	            numericOnly: true,
	            minlength: 10,
	            maxlength: 12
	        },
	        address: {
	            required: true,
	            all: true
	        },
	        city: {
	            required: true,
	            space: true
	        },
	        state: {
	            required: true
	        },
	        pincode: {
	            required: true,
	            space: true,
	            numericOnly: true
	        }
	    },
	    messages: {
	        name: {
	            required: 'Name is required',
	            lettersonly: 'Please enter a valid name'
	        },
	        email: {
	            required: 'Email is required',
	            space: 'Spaces are not allowed',
	            email: 'Please enter a valid email'
	        },
	        mobileNumber: {
	            required: 'Mobile number is required',
	            space: 'Spaces are not allowed',
	            numericOnly: 'Please enter a valid mobile number',
	            minlength: 'Minimum 10 digits required',
	            maxlength: 'Maximum 12 digits allowed'
	        },
	        address: {
	            required: 'Address is required',
	            all: 'Invalid address'
	        },
	        city: {
	            required: 'City is required',
	            space: 'Spaces are not allowed'
	        },
	        state: {
	            required: 'State is required'
	        },
	        pincode: {
	            required: 'Pincode is required',
	            space: 'Spaces are not allowed',
	            numericOnly: 'Please enter a valid pincode'
	        }
	    }
	})
	
	
	// Update Password Validation
	
	var $updatePassword=$("#updatePassword");
	
	$updatePassword.validate({
			
			rules:{
				password: {
					required: true,
					space: true,
					notEqualTo: ['#confirmPassword', '#newPassword']
				},
				newPassword: {
					required: true,
					space: true,
					notEqualTo: '#password'
				},
				confirmPassword: {
					required: true,
					space: true,
					equalTo: '#newPassword'
	
				}
			},
			messages:{
			   password: {
					required: 'password must be required',
					space: 'space not allowed',
					notEqualTo: 'current password and new password must be different'
				},
				newPassword: {
					required: 'new password must be required',
					space: 'space not allowed',
					notEqualTo: 'current password and new password must be different'
				},
				confirmPassword: {
					required: 'confirm password must be required',
					space: 'space not allowed',
					equalTo: 'password mismatch'
	
				}
			}	
	})
	
	// Order Validation
	
	var $order=$("#order");
	
	$order.validate({
			
			rules:{
				firstName:{
					required:true,
					lettersonly:true
				},
				lastName:{
					required:true,
					lettersonly:true
				},
				email: {
					required: true,
					space: true,
					email: true
				},
				mobileNumber: {
					required: true,
					space: true,
					numericOnly: true,
					minlength: 10,
					maxlength: 12

				},
				address: {
					required: true,
					all: true

				},

				city: {
					required: true,
					space: true

				},
				state: {
					required: true,


				},
				pincode: {
					required: true,
					space: true,
					numericOnly: true

				},
				paymentType: {
					required: true
				}
			},
			messages:{
				firstName:{
					required:'firstName required',
					lettersonly:'invalid firstName'
				},
				lastName:{
					required:'lastName required',
					lettersonly:'invalid lastName'
				},
				email: {
					required: 'email id must be required',
					space: 'space not allowed',
					email: 'Invalid email'
				},
				mobileNumber: {
					required: 'mobile number must be required',
					space: 'space not allowed',
					numericOnly: 'invalid mob no',
					minlength: 'min 10 digit',
					maxlength: 'max 12 digit'
				},
				address: {
					required: 'address must be required',
					all: 'invalid'
				},

				city: {
					required: 'city must be required',
					space: 'space not allowed'
				},
				state: {
					required: 'state must be required',
					space: 'space not allowed'
				},
				pincode: {
					required: 'pincode must be required',
					space: 'space not allowed',
					numericOnly: 'invalid pincode'
				},
				paymentType: {
					required: 'Payment Type must be required'
				}
			}	
	})
	
	
	/* Add Product Validation*/
	
	var $addProduct=$("#addProduct");
	
	$addProduct.validate({
			
			rules:{
				productName: {
					required: true,
					minlength: 3
				},
				description: {
					required: true,
					minlength: 10
				},
				category: {
					required: true,
					notEqual: "--select--"
				},
				price: {
					required: true,
					number: true,
					min: 0.01
				},
				isActive: {
					required: true
				},
				stock: {
					required: true,
					digits: true,
					min: 0
				},
				file: {
					required: true,
					extension: "jpg|jpeg|png|gif"
				}
			},
			messages:{
				productName: {
					required: "Product name is required",
					minlength: "Product name must be at least 3 characters long"
				},
				description: {
					required: "Description is required",
					minlength: "Description must be at least 10 characters long"
				},
				category: {
					required: "Please select a category",
					notEqual: "Please select a valid category"
				},
				price: {
					required: "Price is required",
					number: "Please enter a valid price",
					min: "Price must be greater than 0"
				},
				isActive: {
					required: "Please select a status"
				},
				stock: {
					required: "Stock is required",
					digits: "Stock must be a whole number",
					min: "Stock cannot be negative"
				},
				file: {
					required: "Please upload an image",
					extension: "Only image files (jpg, jpeg, png, gif) are allowed"
				}
			}
	})
	
	/* Add Admin*/
	
	var $addAdmin = $("#addAdmin")
	
	$addAdmin.validate({
		rules:{
			name:{
				required:true,
				lettersonly:true
			},
			email: {
				required: true,
				space: true,
				email: true
			},
			mobileNumber: {
				required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12

			},
			password: {
				required: true,
				space: true

			},
			confirmpassword: {
				required: true,
				space: true,
				equalTo: '#password'

			},
			address: {
				required: true,
				all: true

			},

			city: {
				required: true,
				space: true

			},
			state: {
				required: true,


			},
			pincode: {
				required: true,
				space: true,
				numericOnly: true

			}, img: {
				required: true,
			}
		},
		messages:{
			name:{
				required:'name required',
				lettersonly:'invalid name'
			},
			email: {
				required: 'email id must be required',
				space: 'space not allowed',
				email: 'Invalid email'
			},
			mobileNumber: {
				required: 'mobile number must be required',
				space: 'space not allowed',
				numericOnly: 'invalid mob no',
				minlength: 'min 10 digit',
				maxlength: 'max 12 digit'
			},

			password: {
				required: 'password must be required',
				space: 'space not allowed'

			},
			confirmpassword: {
				required: 'confirm password must be required',
				space: 'space not allowed',
				equalTo: 'password mismatch'

			},
			address: {
				required: 'address must be required',
				all: 'invalid'

			},

			city: {
				required: 'city must be required',
				space: 'space not allowed'

			},
			state: {
				required: 'state must be required',
				space: 'space not allowed'

			},
			pincode: {
				required: 'pincode must be required',
				space: 'space not allowed',
				numericOnly: 'invalid pincode'

			},
			img: {
				required: 'image required',
			}
		}
	})
	
	// Add Category Validation
	
	var $addCategory=$("#addCategory");
	
	$addCategory.validate({
			
			rules: {
            name: {
                required: true,
                minlength: 3
            },
            isActive: {
                required: true
            },
            file: {
                required: true,
                extension: "jpg|jpeg|png|gif"
            }
        },
        messages: {
            name: {
                required: "Category name is required",
                minlength: "Category name must be at least 3 characters long"
            },
            isActive: {
                required: "Please select a status"
            },
            file: {
                required: "Please upload an image",
                extension: "Only image files (jpg, jpeg, png, gif) are allowed"
            }
        }
	})
	
	
	// Edit Category Validation
	
	var $editCategory=$("#editCategory");
	
	$editCategory.validate({
			
			rules: {
	            name: {
	                required: true,
	                minlength: 3
	            },
	            isActive: {
	                required: true
	            },
	            file: {
	                extension: "jpg|jpeg|png|gif"
	            }
	        },
	        messages: {
	            name: {
	                required: "Category name is required",
	                minlength: "Category name must be at least 3 characters long"
	            },
	            isActive: {
	                required: "Please select a status"
	            },
	            file: {
	                extension: "Only image files (jpg, jpeg, png, gif) are allowed"
	            }
	        }
	})
	
	/* Update Product Validation*/
	
	var $updateProduct=$("#updateProduct");
	
	$updateProduct.validate({
			
			rules:{
				productName: {
					required: true,
					minlength: 3
				},
				description: {
					required: true,
					minlength: 10
				},
				category: {
					required: true,
					notEqual: "--select--"
				},
				price: {
					required: true,
					number: true,
					min: 0.01
				},
				discount: {
					required: true,
					number: true,
					min: 0.01
				},
				isActive: {
					required: true
				},
				stock: {
					required: true,
					digits: true,
					min: 0
				},
				file: {
					required: true,
					extension: "jpg|jpeg|png|gif"
				}
			},
			messages:{
				productName: {
					required: "Product name is required",
					minlength: "Product name must be at least 3 characters long"
				},
				description: {
					required: "Description is required",
					minlength: "Description must be at least 10 characters long"
				},
				category: {
					required: "Please select a category",
					notEqual: "Please select a valid category"
				},
				price: {
					required: "Price is required",
					number: "Please enter a valid price",
					min: "Price must be greater than 0"
				},
				discount: {
					required: "Discount is required",
					number: "Please enter a valid Discount",
					min: "Discount must be greater than 0"
				},
				isActive: {
					required: "Please select a status"
				},
				stock: {
					required: "Stock is required",
					digits: "Stock must be a whole number",
					min: "Stock cannot be negative"
				},
				file: {
					required: "Please upload an image",
					extension: "Only image files (jpg, jpeg, png, gif) are allowed"
				}
			}	
	})
	
})

jQuery.validator.addMethod('lettersonly', function(value, element) {
		return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
	});
	
		jQuery.validator.addMethod('space', function(value, element) {
		return /^[^-\s]+$/.test(value);
	});

	jQuery.validator.addMethod('all', function(value, element) {
		return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
	});


	jQuery.validator.addMethod('numericOnly', function(value, element) {
		return /^[0-9]+$/.test(value);
	});
	
$.validator.addMethod("notEqualTo", function(value, element, param) {
    if (Array.isArray(param)) {
        for (var i = 0; i < param.length; i++) {
            if (value === $(param[i]).val()) {
                return false;
            }
        }
        return true;
    } else {
        return value !== $(param).val();
    }
}, "Passwords must be different");

$.validator.addMethod("notEqual", function (value, element, arg) {
        return arg !== value;
    }, "Please select a valid option");