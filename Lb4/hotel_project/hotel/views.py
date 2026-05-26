from django.shortcuts import render
from django.shortcuts import redirect

from .models import (
    Room,
    Booking,
    Client
)


def home(request):

    bookings = Booking.objects.all()

    return render(
        request,
        'home.html',
        {
            'bookings': bookings
        }
    )


def available_rooms(request):

    rooms = Room.objects.all()

    return render(
        request,
        'available_rooms.html',
        {
            'rooms': rooms
        }
    )


def create_booking(request):

    if request.method == 'POST':

        client_name = request.POST.get(
            'client_name'
        )

        phone = request.POST.get(
            'phone'
        )

        room_id = request.POST.get(
            'room_id'
        )

        check_in = request.POST.get(
            'check_in'
        )

        check_out = request.POST.get(
            'check_out'
        )

        room = Room.objects.get(
            id=room_id
        )

        existing_booking = Booking.objects.filter(
            room=room,
            check_in__lte=check_out,
            check_out__gte=check_in
        ).exists()

        if existing_booking:

            return render(
                request,
                'create_booking.html',
                {
                    'rooms': Room.objects.all(),
                    'error':
                        'Ця кімната вже заброньована на обрані дати'
                }
            )

        client = Client.objects.create(
            full_name=client_name,
            phone=phone
        )

        Booking.objects.create(
            client=client,
            room=room,
            check_in=check_in,
            check_out=check_out
        )

        return redirect('home')

    rooms = Room.objects.all()

    return render(
        request,
        'create_booking.html',
        {
            'rooms': rooms
        }
    )

def delete_booking(request, id):

    booking = Booking.objects.get(
        id=id
    )

    booking.delete()

    return redirect('home')