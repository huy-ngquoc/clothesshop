(function ($) {
    // Spinner
    const spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();


    // Fixed Navbar
    $(window).on("scroll", function () {
        const windowWidth = $(window).width();
        if (windowWidth == null) {
            return;
        }

        const scrollTop = $(this).scrollTop();
        if (scrollTop == null) {
            return;
        }

        if (windowWidth < 992) {
            if (scrollTop > 55) {
                $('.fixed-top').addClass('shadow');
            } else {
                $('.fixed-top').removeClass('shadow');
            }
        } else {
            if (scrollTop > 55) {
                $('.fixed-top').addClass('shadow').css('top', 0);
            } else {
                $('.fixed-top').removeClass('shadow').css('top', 0);
            }
        }
    });


    // Back to top button
    $(window).on("scroll", function () {
        const scrollTop = $(this).scrollTop();
        if (scrollTop == null) {
            return;
        }

        if (scrollTop > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').on("click", function () {
        $('html, body').animate({ scrollTop: 0 }, 1500, 'easeInOutExpo');
        return false;
    });


    // Testimonial carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav: true,
        navText: [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0: {
                items: 1
            },
            576: {
                items: 1
            },
            768: {
                items: 1
            },
            992: {
                items: 2
            },
            1200: {
                items: 2
            }
        }
    });


    // vegetable carousel
    $(".vegetable-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav: true,
        navText: [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0: {
                items: 1
            },
            576: {
                items: 1
            },
            768: {
                items: 2
            },
            992: {
                items: 3
            },
            1200: {
                items: 4
            }
        }
    });


    // Modal Video
    $(function () {
        let $videoSrc = "";
        $('.btn-play').on("click", function () {
            $videoSrc = $(this).data("src") as string ?? "";
        });
        console.log($videoSrc);

        $('#videoModal').on('shown.bs.modal', function () {
            $("#video").attr('src', $videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0");
        })

        $('#videoModal').on('hide.bs.modal', function () {
            $("#video").attr('src', $videoSrc);
        })
    });



    // Product Quantity
    $('.quantity button').on('click', function () {
        let button = $(this);
        let oldValue = button.parent().parent().find('input').val() ?? "";

        let newVal = 0;

        let oldValueFloat = parseFloat(oldValue);
        if (!Number.isNaN(oldValueFloat)) {
            if (button.hasClass('btn-plus')) {
                newVal = oldValueFloat + 1;
            } else if (oldValueFloat > 0) {
                newVal = parseFloat(oldValue) - 1;
            }
            else {
                // Do nothing.
            }
        }

        button.parent().parent().find('input').val(newVal);
    });

})(jQuery);

