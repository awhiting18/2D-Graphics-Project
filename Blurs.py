from PIL import Image, ImageDraw


def blur(image, kernel):
    # Load image:
    input_image = Image.open(image)
    input_pixels = input_image.load()

    # Middle of the kernel
    offset = len(kernel) // 2

    # Create output image
    output_image = Image.new("RGB", input_image.size)
    draw = ImageDraw.Draw(output_image)

    # Compute convolution between intensity and kernels
    for x in range(offset, input_image.width - offset):
        for y in range(offset, input_image.height - offset):
            acc = [0, 0, 0]
            for a in range(len(kernel)):
                for b in range(len(kernel)):
                    xn = x + a - offset
                    yn = y + b - offset
                    pixel = input_pixels[xn, yn]
                    acc[0] += pixel[0] * kernel[a][b]
                    acc[1] += pixel[1] * kernel[a][b]
                    acc[2] += pixel[2] * kernel[a][b]

            draw.point((x, y), (int(acc[0]), int(acc[1]), int(acc[2])))

    output_image.save("output.jpg")


def blurIteration(inputImage, outputImage, kernal, iterations):
    print("Working on iteration 1")
    blur(inputImage, kernal)
    print("Iteration 1 done")
    for x in range(1, iterations):
        print("Working on iteration ", x + 1)
        blur(outputImage, kernal)
        print("Iteration ", x + 1, " done")

        # Box Blur kernel
box_kernel3x3 = [[1 / 9, 1 / 9, 1 / 9],
                 [1 / 9, 1 / 9, 1 / 9],
                 [1 / 9, 1 / 9, 1 / 9]]

box_kernel9x9 = [[1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81],
                 [1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81],
                 [1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81],
                 [1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81],
                 [1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81],
                 [1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81],
                 [1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81],
                 [1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81],
                 [1 / 81, 1 / 81, 1 / 81, 1/81, 1/81, 1/81, 1/81, 1/81, 1/81], ]

# Gaussian kernel
gaussian_kernel = [[1 / 256, 4 / 256,  6 / 256,  4 / 256, 1 / 256],
                   [4 / 256, 16 / 256, 24 / 256, 16 / 256, 4 / 256],
                   [6 / 256, 24 / 256, 36 / 256, 24 / 256, 6 / 256],
                   [4 / 256, 16 / 256, 24 / 256, 16 / 256, 4 / 256],
                   [1 / 256, 4 / 256,  6 / 256,  4 / 256, 1 / 256]]

# Select kernel here:
kernel = box_kernel3x3

blurIteration("oldCar.jpg", "output.jpg", kernel, 3)
