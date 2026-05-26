number = int(input("Введіть число: "))

if number <= 1:
    print("Число не є простим")
else:
    is_prime = True

    for i in range(2, number):
        if number % i == 0:
            is_prime = False
            break

    if is_prime:
        print("Число є простим")
    else:
        print("Число не є простим")