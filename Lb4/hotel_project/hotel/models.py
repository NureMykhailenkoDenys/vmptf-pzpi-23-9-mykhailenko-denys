from django.db import models


class Hotel(models.Model):

    name = models.CharField(
        max_length=100
    )

    address = models.CharField(
        max_length=200
    )

    def __str__(self):

        return self.name


class Service(models.Model):

    name = models.CharField(
        max_length=100
    )

    price = models.DecimalField(
        max_digits=10,
        decimal_places=2
    )

    def __str__(self):

        return self.name


class Room(models.Model):

    hotel = models.ForeignKey(
        Hotel,
        on_delete=models.CASCADE,
        related_name="rooms"
    )

    number = models.IntegerField()

    price = models.DecimalField(
        max_digits=10,
        decimal_places=2
    )

    services = models.ManyToManyField(
        Service,
        blank=True
    )

    def __str__(self):

        return f"Room {self.number}"


class Client(models.Model):

    full_name = models.CharField(
        max_length=100
    )

    phone = models.CharField(
        max_length=20
    )

    def __str__(self):

        return self.full_name


class Booking(models.Model):

    client = models.ForeignKey(
        Client,
        on_delete=models.CASCADE
    )

    room = models.ForeignKey(
        Room,
        on_delete=models.CASCADE
    )

    check_in = models.DateField()

    check_out = models.DateField()

    created_at = models.DateTimeField(
        auto_now_add=True
    )

    def __str__(self):

        return f"{self.client} - {self.room}"