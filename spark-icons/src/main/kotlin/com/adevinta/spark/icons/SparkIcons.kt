/*
 * Copyright (c) 2023 Adevinta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
@file:Suppress("UnusedReceiverParameter", "unused", "ktlint")

package com.adevinta.spark.icons

import com.adevinta.spark.icons.SparkIcon.DrawableRes

/**
 * A collection of static icons from Spark.
 *
 * This object provides access to all static vector icons as drawable resources.
 * Each icon is available as a property that returns a [SparkIcon.DrawableRes] or a [SparkIcon.Vector],
 * ensuring type safety and consistency across the icon system.
 *
 * @see SparkIcon.DrawableRes
 * @see SparkIcon.Vector
 */
public object SparkIcons

public val SparkIcons.AccessoriesCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_accessories_criteria)

@Deprecated("AddFill has been renamed AddCircleFill", ReplaceWith("SparkIcons.AddCircleFill"))
public val SparkIcons.AddFill: DrawableRes get() = AddCircleFill
public val SparkIcons.AddCircleFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_add_circle_fill)

@Deprecated("AddOutline has been renamed AddCircleOutline", ReplaceWith("SparkIcons.AddCircleOutline"))
public val SparkIcons.AddOutline: DrawableRes get() = AddCircleOutline
public val SparkIcons.AddCircleOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_add_circle_outline)

@Deprecated("AllDirection has been renamed AllDirectionArrows", ReplaceWith("SparkIcons.AllDirectionArrows"))
public val SparkIcons.AllDirection: DrawableRes get() = AllDirectionArrows
public val SparkIcons.AllDirectionArrows: DrawableRes get() = DrawableRes(R.drawable.spark_icons_all_direction_arrows)

@Deprecated("MapExpand has been renamed ArrowExpand", ReplaceWith("SparkIcons.ArrowExpand"))
public val SparkIcons.MapExpand: DrawableRes get() = ArrowExpand
public val SparkIcons.ArrowExpand: DrawableRes get() = DrawableRes(R.drawable.spark_icons_arrow_expand)
public val SparkIcons.ArrowReduce: DrawableRes get() = DrawableRes(R.drawable.spark_icons_arrow_reduce)
public val SparkIcons.AtticCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_attic_criteria)
public val SparkIcons.AxlesCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_axles_criteria)
public val SparkIcons.BabyBedCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_baby_bed_criteria)
public val SparkIcons.BabyBottle: DrawableRes get() = DrawableRes(R.drawable.spark_icons_baby_bottle)
public val SparkIcons.BabyBottleCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_baby_bottle_criteria)
public val SparkIcons.BalconyCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_balcony_criteria)
public val SparkIcons.Ball: DrawableRes get() = DrawableRes(R.drawable.spark_icons_ball)

@Deprecated("Block has been renamed Banned", ReplaceWith("SparkIcons.Banned"))
public val SparkIcons.Block: DrawableRes get() = Banned
public val SparkIcons.Banned: DrawableRes get() = DrawableRes(R.drawable.spark_icons_banned)
public val SparkIcons.BarrowCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_barrow_criteria)
public val SparkIcons.BathtubCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bathtub_criteria)
public val SparkIcons.BearCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bear_criteria)
public val SparkIcons.BedCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bed_criteria)

@Deprecated("HotelFill has been renamed BedFill", ReplaceWith("SparkIcons.BedFill"))
public val SparkIcons.HotelFill: DrawableRes get() = BedFill

public val SparkIcons.BedFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bed_fill)
public val SparkIcons.BedLinen: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bed_linen)

@Deprecated("HotelOutline has been renamed BedOutline", ReplaceWith("SparkIcons.BedOutline"))
public val SparkIcons.HotelOutline: DrawableRes get() = BedOutline
public val SparkIcons.BedOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bed_outline)
public val SparkIcons.BellCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_criteria)

@Deprecated("AlarmFill has been renamed BellFill", ReplaceWith("SparkIcons.BellFill"))
public val SparkIcons.AlarmFill: DrawableRes get() = BellFill
public val SparkIcons.BellFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_fill)

@Deprecated("AlarmOffFill has been renamed BellOffFill", ReplaceWith("SparkIcons.BellOffFill"))
public val SparkIcons.AlarmOffFill: DrawableRes get() = BellOffFill
public val SparkIcons.BellOffFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_off_fill)

@Deprecated("AlarmOffOutline has been renamed BellOffOutline", ReplaceWith("SparkIcons.BellOffOutline"))
public val SparkIcons.AlarmOffOutline: DrawableRes get() = BellOffOutline
public val SparkIcons.BellOffOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_off_outline)

@Deprecated("AlarmOnFill has been renamed BellOnFill", ReplaceWith("SparkIcons.BellOnFill"))
public val SparkIcons.AlarmOnFill: DrawableRes get() = BellOnFill
public val SparkIcons.BellOnFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_on_fill)

@Deprecated("AlarmOnOutline has been renamed BellOnOutline", ReplaceWith("SparkIcons.BellOnOutline"))
public val SparkIcons.AlarmOnOutline: DrawableRes get() = BellOnOutline
public val SparkIcons.BellOnOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_on_outline)

@Deprecated("AlarmOutline has been renamed BellOutline", ReplaceWith("SparkIcons.BellOutline"))
public val SparkIcons.AlarmOutline: DrawableRes get() = BellOutline
public val SparkIcons.BellOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_outline)

@Deprecated("NotificationFill has been renamed BellShakeFill", ReplaceWith("SparkIcons.BellShakeFill"))
public val SparkIcons.NotificationFill: DrawableRes get() = BellShakeFill
public val SparkIcons.BellShakeFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_shake_fill)

@Deprecated("NotificationOutline has been renamed BellShakeOutline", ReplaceWith("SparkIcons.BellShakeOutline"))
public val SparkIcons.NotificationOutline: DrawableRes get() = BellShakeOutline
public val SparkIcons.BellShakeOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bell_shake_outline)
public val SparkIcons.BicycleCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bicycle_criteria)
public val SparkIcons.BikeSizeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bike_size_criteria)
public val SparkIcons.BirdCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bird_criteria)
public val SparkIcons.BirthdayCakeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_birthday_cake_criteria)
public val SparkIcons.BluetoothCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bluetooth_criteria)
public val SparkIcons.BoatCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_boat_criteria)
public val SparkIcons.BodySuit: DrawableRes get() = DrawableRes(R.drawable.spark_icons_body_suit)
public val SparkIcons.BookCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_book_criteria)
public val SparkIcons.BoxCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_box_criteria)
public val SparkIcons.Boxes: DrawableRes get() = DrawableRes(R.drawable.spark_icons_boxes)
public val SparkIcons.BrandCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_brand_criteria)
public val SparkIcons.Briefcase: DrawableRes get() = DrawableRes(R.drawable.spark_icons_briefcase)
public val SparkIcons.BriefcaseCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_briefcase_criteria)
public val SparkIcons.BubbleChatFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_chat_fill)
public val SparkIcons.BubbleChatOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_chat_outline)
public val SparkIcons.BubbleCheck: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_check)

@Deprecated("TypingFill has been renamed BubbleDotsFill", ReplaceWith("SparkIcons.BubbleDotsFill"))
public val SparkIcons.TypingFill: DrawableRes get() = BubbleDotsFill
public val SparkIcons.BubbleDotsFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_dots_fill)

@Deprecated("TypingOutline has been renamed BubbleDotsOutline", ReplaceWith("SparkIcons.BubbleDotsOutline"))
public val SparkIcons.TypingOutline: DrawableRes get() = BubbleDotsOutline
public val SparkIcons.BubbleDotsOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_dots_outline)
public val SparkIcons.BubbleLightning: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_lightning)

@Deprecated("MessageFill has been renamed BubbleTextFill", ReplaceWith("SparkIcons.BubbleTextFill"))
public val SparkIcons.MessageFill: DrawableRes get() = BubbleTextFill
public val SparkIcons.BubbleTextFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_text_fill)

@Deprecated("MessageOutline has been renamed BubbleTextOutline", ReplaceWith("SparkIcons.BubbleTextOutline"))
public val SparkIcons.MessageOutline: DrawableRes get() = BubbleTextOutline
public val SparkIcons.BubbleTextOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_text_outline)
public val SparkIcons.BubbleWarningFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_warning_fill)
public val SparkIcons.BubbleWarningOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_bubble_warning_outline)
public val SparkIcons.Building: DrawableRes get() = DrawableRes(R.drawable.spark_icons_building)
public val SparkIcons.BuildingCircle: DrawableRes get() = DrawableRes(R.drawable.spark_icons_building_circle)
public val SparkIcons.BuildingSquare: DrawableRes get() = DrawableRes(R.drawable.spark_icons_building_square)
public val SparkIcons.BuildingsCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_buildings_criteria)

@Deprecated("CalculateFill has been renamed CalculatorFill", ReplaceWith("SparkIcons.CalculatorFill"))
public val SparkIcons.CalculateFill: DrawableRes get() = CalculatorFill

@Deprecated("CalculateOutline has been renamed CalculatorOutline", ReplaceWith("SparkIcons.CalculatorOutline"))
public val SparkIcons.CalculateOutline: DrawableRes get() = CalculatorOutline
public val SparkIcons.CalculatorFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_calculator_fill)
public val SparkIcons.CalculatorOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_calculator_outline)

@Deprecated("CalendarValidFill has been renamed CalendarCheckFill", ReplaceWith("SparkIcons.CalendarCheckFill"))
public val SparkIcons.CalendarValidFill: DrawableRes get() = CalendarCheckFill
public val SparkIcons.CalendarCheckFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_calendar_check_fill)

@Deprecated(
    "CalendarValidOutline has been renamed CalendarCheckOutline",
    ReplaceWith("SparkIcons.CalendarCheckOutline"),
)
public val SparkIcons.CalendarValidOutline: DrawableRes get() = CalendarCheckOutline
public val SparkIcons.CalendarCheckOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_calendar_check_outline)
public val SparkIcons.CalendarCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_calendar_criteria)

@Deprecated("CalendarFill has been renamed CalendarTextFill", ReplaceWith("SparkIcons.CalendarTextFill"))
public val SparkIcons.CalendarFill: DrawableRes get() = CalendarTextFill
public val SparkIcons.CalendarTextFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_calendar_text_fill)

@Deprecated("CalendarOutline has been renamed CalendarTextOutline", ReplaceWith("SparkIcons.CalendarTextOutline"))
public val SparkIcons.CalendarOutline: DrawableRes get() = CalendarTextOutline
public val SparkIcons.CalendarTextOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_calendar_text_outline)

@Deprecated("Lens has been renamed CameraLens", ReplaceWith("SparkIcons.CameraLens"))
public val SparkIcons.Lens: DrawableRes get() = CameraLens
public val SparkIcons.CameraLens: DrawableRes get() = DrawableRes(R.drawable.spark_icons_camera_lens)
public val SparkIcons.Camper: DrawableRes get() = DrawableRes(R.drawable.spark_icons_camper)
public val SparkIcons.CarBatteryCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_car_battery_criteria)
public val SparkIcons.CarCloseupCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_car_closeup_criteria)
public val SparkIcons.CarTypeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_car_type_criteria)
public val SparkIcons.CardBoard: DrawableRes get() = DrawableRes(R.drawable.spark_icons_card_board)

@Deprecated("Card has been renamed CardBrand", ReplaceWith("SparkIcons.CardBrand"))
public val SparkIcons.Card: DrawableRes get() = CardBrand
public val SparkIcons.CardBrand: DrawableRes get() = DrawableRes(R.drawable.spark_icons_card_brand)
public val SparkIcons.CaveCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_cave_criteria)

@Deprecated("Link has been renamed Chain", ReplaceWith("SparkIcons.Chain"))
public val SparkIcons.Link: DrawableRes get() = Chain
public val SparkIcons.Chain: DrawableRes get() = DrawableRes(R.drawable.spark_icons_chain)

@Deprecated(
    "ArrowHorizontalDown has been renamed ChevronHorizontalDown",
    ReplaceWith("SparkIcons.ChevronHorizontalDown"),
)
public val SparkIcons.ArrowHorizontalDown: DrawableRes get() = ChevronHorizontalDown
public val SparkIcons.ChevronHorizontalDown: DrawableRes get() = DrawableRes(R.drawable.spark_icons_chevron_horizontal_down)

@Deprecated("ArrowHorizontalUp has been renamed ChevronHorizontalUp", ReplaceWith("SparkIcons.ChevronHorizontalUp"))
public val SparkIcons.ArrowHorizontalUp: DrawableRes get() = ChevronHorizontalUp
public val SparkIcons.ChevronHorizontalUp: DrawableRes get() = DrawableRes(R.drawable.spark_icons_chevron_horizontal_up)

@Deprecated("ArrowVerticalLeft has been renamed ChevronVerticalLeft", ReplaceWith("SparkIcons.ChevronVerticalLeft"))
public val SparkIcons.ArrowVerticalLeft: DrawableRes get() = ChevronVerticalLeft
public val SparkIcons.ChevronVerticalLeft: DrawableRes get() = DrawableRes(R.drawable.spark_icons_chevron_vertical_left)

@Deprecated("ArrowVerticalRight has been renamed ChevronVerticalRight", ReplaceWith("SparkIcons.ChevronVerticalRight"))
public val SparkIcons.ArrowVerticalRight: DrawableRes get() = ChevronVerticalRight
public val SparkIcons.ChevronVerticalRight: DrawableRes get() = DrawableRes(R.drawable.spark_icons_chevron_vertical_right)

@Deprecated("ValidFill has been renamed CircleCheckFill", ReplaceWith("SparkIcons.CircleCheckFill"))
public val SparkIcons.ValidFill: DrawableRes get() = CircleCheckFill
public val SparkIcons.CircleCheckFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_circle_check_fill)

@Deprecated("ValidOutline has been renamed CircleCheckOutline", ReplaceWith("SparkIcons.CircleCheckOutline"))
public val SparkIcons.ValidOutline: DrawableRes get() = CircleCheckOutline
public val SparkIcons.CircleCheckOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_circle_check_outline)

@Deprecated("DeleteFill has been renamed CircleCrossFill", ReplaceWith("SparkIcons.CircleCrossFill"))
public val SparkIcons.DeleteFill: DrawableRes get() = CircleCrossFill
public val SparkIcons.CircleCrossFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_circle_cross_fill)

@Deprecated("DeleteOutline has been renamed CircleCrossOutline", ReplaceWith("SparkIcons.CircleCrossOutline"))
public val SparkIcons.DeleteOutline: DrawableRes get() = CircleCrossOutline
public val SparkIcons.CircleCrossOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_circle_cross_outline)
public val SparkIcons.CircleWeightCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_circle_weight_criteria)
public val SparkIcons.ClassCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_class_criteria)

@Deprecated("ProCursorFill has been renamed ClickCursorFill", ReplaceWith("SparkIcons.ClickCursorFill"))
public val SparkIcons.ProCursorFill: DrawableRes get() = ClickCursorFill
public val SparkIcons.ClickCursorFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_click_cursor_fill)

@Deprecated("ProCursorOutline has been renamed ClickCursorOutline", ReplaceWith("SparkIcons.ClickCursorOutline"))
public val SparkIcons.ProCursorOutline: DrawableRes get() = ClickCursorOutline
public val SparkIcons.ClickCursorOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_click_cursor_outline)
public val SparkIcons.ClockCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_clock_criteria)
public val SparkIcons.ClosureCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_closure_criteria)
public val SparkIcons.CoffeeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_coffee_criteria)

@Deprecated("MoneyFill has been renamed CoinsFill", ReplaceWith("SparkIcons.CoinsFill"))
public val SparkIcons.MoneyFill: DrawableRes get() = CoinsFill
public val SparkIcons.CoinsFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_coins_fill)

@Deprecated("MoneyOutline has been renamed CoinsOutline", ReplaceWith("SparkIcons.CoinsOutline"))
public val SparkIcons.MoneyOutline: DrawableRes get() = CoinsOutline
public val SparkIcons.CoinsOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_coins_outline)
public val SparkIcons.CommonHouseCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_common_house_criteria)
public val SparkIcons.CompassCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_compass_criteria)
public val SparkIcons.ConnectedWatch: DrawableRes get() = DrawableRes(R.drawable.spark_icons_connected_watch)
public val SparkIcons.Controller: DrawableRes get() = DrawableRes(R.drawable.spark_icons_controller)
public val SparkIcons.ControllerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_controller_criteria)
public val SparkIcons.CoverageCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_coverage_criteria)
public val SparkIcons.Cradle: DrawableRes get() = DrawableRes(R.drawable.spark_icons_cradle)

@Deprecated("CardFill has been renamed CreditCardFill", ReplaceWith("SparkIcons.CreditCardFill"))
public val SparkIcons.CardFill: DrawableRes get() = CreditCardFill

public val SparkIcons.CreditCardFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_credit_card_fill)

@Deprecated("CardOutline has been renamed CreditCardOutline", ReplaceWith("SparkIcons.CreditCardOutline"))
public val SparkIcons.CardOutline: DrawableRes get() = CreditCardOutline
public val SparkIcons.CreditCardOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_credit_card_outline)
public val SparkIcons.Critair: DrawableRes get() = DrawableRes(R.drawable.spark_icons_critair)

@Deprecated("Close has been renamed Cross", ReplaceWith("SparkIcons.Cross"))
public val SparkIcons.Close: DrawableRes get() = Cross
public val SparkIcons.Cross: DrawableRes get() = DrawableRes(R.drawable.spark_icons_cross)

@Deprecated("MapCursorFill has been renamed CursorFill", ReplaceWith("SparkIcons.CursorFill"))
public val SparkIcons.MapCursorFill: DrawableRes get() = CursorFill
public val SparkIcons.CursorFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_cursor_fill)

@Deprecated("MapCursorOutline has been renamed CursorOutline", ReplaceWith("SparkIcons.CursorOutline"))
public val SparkIcons.MapCursorOutline: DrawableRes get() = CursorOutline
public val SparkIcons.CursorOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_cursor_outline)
public val SparkIcons.CutleryCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_cutlery_criteria)
public val SparkIcons.CylindricalCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_cylindrical_criteria)
public val SparkIcons.DiamondCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_diamond_criteria)
public val SparkIcons.DigicodeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_digicode_criteria)
public val SparkIcons.Disconnect: DrawableRes get() = DrawableRes(R.drawable.spark_icons_disconnect)

@Deprecated("DissatisfiedFill has been renamed DissatisfiedFaceFill", ReplaceWith("SparkIcons.DissatisfiedFaceFill"))
public val SparkIcons.DissatisfiedFill: DrawableRes get() = DissatisfiedFaceFill

@Deprecated(
    "DissatisfiedOutline has been renamed DissatisfiedFaceOutline",
    ReplaceWith("SparkIcons.DissatisfiedFaceOutline"),
)
public val SparkIcons.DissatisfiedOutline: DrawableRes get() = DissatisfiedFaceOutline
public val SparkIcons.DissatisfiedFaceFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_dissatisfied_face_fill)
public val SparkIcons.DissatisfiedFaceOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_dissatisfied_face_outline)
public val SparkIcons.DoorsCarCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_doors_car_criteria)

@Deprecated("ArrowDoubleLeft has been renamed DoubleChevronsLeft", ReplaceWith("SparkIcons.DoubleChevronsLeft"))
public val SparkIcons.ArrowDoubleLeft: DrawableRes get() = DoubleChevronsLeft
public val SparkIcons.DoubleChevronsLeft: DrawableRes get() = DrawableRes(R.drawable.spark_icons_double_chevrons_left)

@Deprecated("ArrowDoubleRight has been renamed DoubleChevronsRight", ReplaceWith("SparkIcons.DoubleChevronsRight"))
public val SparkIcons.ArrowDoubleRight: DrawableRes get() = DoubleChevronsRight
public val SparkIcons.DoubleChevronsRight: DrawableRes get() = DrawableRes(R.drawable.spark_icons_double_chevrons_right)

@Deprecated("DownloadFill has been renamed DownloadFileFill", ReplaceWith("SparkIcons.DownloadFileFill"))
public val SparkIcons.DownloadFill: DrawableRes get() = DownloadFileFill
public val SparkIcons.DownloadFileFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_download_file_fill)

@Deprecated("DownloadOutline has been renamed DownloadFileOutline", ReplaceWith("SparkIcons.DownloadFileOutline"))
public val SparkIcons.DownloadOutline: DrawableRes get() = DownloadFileOutline
public val SparkIcons.DownloadFileOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_download_file_outline)
public val SparkIcons.DumbbellCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_dumbbell_criteria)
public val SparkIcons.DumpTruckCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_dump_truck_criteria)
public val SparkIcons.DuplexCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_duplex_criteria)
public val SparkIcons.ElectricalPlug: DrawableRes get() = DrawableRes(R.drawable.spark_icons_electrical_plug)
public val SparkIcons.ElectricalSocketCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_electrical_socket_criteria)
public val SparkIcons.EnergyCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_energy_criteria)
public val SparkIcons.EngineCarCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_engine_car_criteria)
public val SparkIcons.EuroShieldFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_euro_shield_fill)
public val SparkIcons.EuroShieldOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_euro_shield_outline)

@Deprecated("Euro has been renamed EuroSymbol", ReplaceWith("SparkIcons.EuroSymbol"))
public val SparkIcons.Euro: DrawableRes get() = EuroSymbol
public val SparkIcons.EuroSymbol: DrawableRes get() = DrawableRes(R.drawable.spark_icons_euro_symbol)
public val SparkIcons.EurosCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_euros_criteria)
public val SparkIcons.ExcavatorCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_excavator_criteria)
public val SparkIcons.FabricCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_fabric_criteria)

@Deprecated("FacebookFill has been renamed FacebookLogoFill", ReplaceWith("SparkIcons.FacebookLogoFill"))
public val SparkIcons.FacebookFill: DrawableRes get() = FacebookLogoFill
public val SparkIcons.FacebookLogoFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_facebook_logo_fill)

@Deprecated("FacebookOutline has been renamed FacebookLogoOutline", ReplaceWith("SparkIcons.FacebookLogoOutline"))
public val SparkIcons.FacebookOutline: DrawableRes get() = FacebookLogoOutline
public val SparkIcons.FacebookLogoOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_facebook_logo_outline)
public val SparkIcons.FamilyCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_family_criteria)

@Deprecated("CvFill has been renamed FileFill", ReplaceWith("SparkIcons.FileFill"))
public val SparkIcons.CvFill: DrawableRes get() = FileFill
public val SparkIcons.FileFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_file_fill)

@Deprecated("CvOutline has been renamed FileOutline", ReplaceWith("SparkIcons.FileOutline"))
public val SparkIcons.CvOutline: DrawableRes get() = FileOutline
public val SparkIcons.FileOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_file_outline)
public val SparkIcons.FireplaceCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_fireplace_criteria)
public val SparkIcons.FlowerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_flower_criteria)

@Deprecated("SendFill has been renamed FoldedEnvelopFill", ReplaceWith("SparkIcons.FoldedEnvelopFill"))
public val SparkIcons.SendFill: DrawableRes get() = FoldedEnvelopFill
public val SparkIcons.FoldedEnvelopFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_folded_envelop_fill)

@Deprecated("SendOutline has been renamed FoldedEnvelopOutline", ReplaceWith("SparkIcons.FoldedEnvelopOutline"))
public val SparkIcons.SendOutline: DrawableRes get() = FoldedEnvelopOutline
public val SparkIcons.FoldedEnvelopOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_folded_envelop_outline)
public val SparkIcons.FootCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_foot_criteria)
public val SparkIcons.FountainPen: DrawableRes get() = DrawableRes(R.drawable.spark_icons_fountain_pen)

@Deprecated("CountryFill has been renamed FranceFill", ReplaceWith("SparkIcons.FranceFill"))
public val SparkIcons.CountryFill: DrawableRes get() = FranceFill
public val SparkIcons.FranceFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_france_fill)

@Deprecated("CountryOutline has been renamed FranceOutline", ReplaceWith("SparkIcons.FranceOutline"))
public val SparkIcons.CountryOutline: DrawableRes get() = FranceOutline
public val SparkIcons.FranceOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_france_outline)
public val SparkIcons.FuelCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_fuel_criteria)
public val SparkIcons.FurnitureCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_furniture_criteria)
public val SparkIcons.FurnitureTypeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_furniture_type_criteria)
public val SparkIcons.GameBoy: DrawableRes get() = DrawableRes(R.drawable.spark_icons_game_boy)
public val SparkIcons.GarageCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_garage_criteria)
public val SparkIcons.GardenCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_garden_criteria)
public val SparkIcons.GardenTools: DrawableRes get() = DrawableRes(R.drawable.spark_icons_garden_tools)

@Deprecated("WheelFill has been renamed GearFill", ReplaceWith("SparkIcons.GearFill"))
public val SparkIcons.WheelFill: DrawableRes get() = GearFill
public val SparkIcons.GearFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_gear_fill)

@Deprecated("WheelOutline has been renamed GearOutline", ReplaceWith("SparkIcons.GearOutline"))
public val SparkIcons.WheelOutline: DrawableRes get() = GearOutline
public val SparkIcons.GearOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_gear_outline)
public val SparkIcons.Gift: DrawableRes get() = DrawableRes(R.drawable.spark_icons_gift)
public val SparkIcons.GlassWindowsCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_glass_windows_criteria)
public val SparkIcons.GpsCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_gps_criteria)
public val SparkIcons.Grapes: DrawableRes get() = DrawableRes(R.drawable.spark_icons_grapes)
public val SparkIcons.GraphicCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_graphic_criteria)
public val SparkIcons.GroundFloorCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_ground_floor_criteria)
public val SparkIcons.Guitar: DrawableRes get() = DrawableRes(R.drawable.spark_icons_guitar)
public val SparkIcons.HandCatCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_hand_cat_criteria)
public val SparkIcons.HandDeliveredCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_hand_delivered_criteria)
public val SparkIcons.HandGift: DrawableRes get() = DrawableRes(R.drawable.spark_icons_hand_gift)
public val SparkIcons.HandGiftCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_hand_gift_criteria)
public val SparkIcons.HappyFaceFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_happy_face_fill)
public val SparkIcons.HappyFaceOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_happy_face_outline)
public val SparkIcons.HeadlightCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_headlight_criteria)

@Deprecated("SupportFill has been renamed HeadphoneFill", ReplaceWith("SparkIcons.HeadphoneFill"))
public val SparkIcons.SupportFill: DrawableRes get() = HeadphoneFill
public val SparkIcons.HeadphoneFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_headphone_fill)

@Deprecated("SupportOutline has been renamed HeadphoneOutline", ReplaceWith("SparkIcons.HeadphoneOutline"))
public val SparkIcons.SupportOutline: DrawableRes get() = HeadphoneOutline
public val SparkIcons.HeadphoneOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_headphone_outline)

@Deprecated("SupportSmileFill has been renamed HeadphoneSmileFill", ReplaceWith("SparkIcons.HeadphoneSmileFill"))
public val SparkIcons.SupportSmileFill: DrawableRes get() = HeadphoneSmileFill
public val SparkIcons.HeadphoneSmileFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_headphone_smile_fill)

@Deprecated(
    "SupportSmileOutline has been renamed HeadphoneSmileOutline",
    ReplaceWith("SparkIcons.HeadphoneSmileOutline"),
)
public val SparkIcons.SupportSmileOutline: DrawableRes get() = HeadphoneSmileOutline
public val SparkIcons.HeadphoneSmileOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_headphone_smile_outline)
public val SparkIcons.HeadsCatCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_heads_cat_criteria)
public val SparkIcons.HeadsDogCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_heads_dog_criteria)

@Deprecated("LikeFill has been renamed HeartFill", ReplaceWith("SparkIcons.HeartFill"))
public val SparkIcons.LikeFill: DrawableRes get() = HeartFill
public val SparkIcons.HeartFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_heart_fill)
public val SparkIcons.HeartHandshake: DrawableRes get() = DrawableRes(R.drawable.spark_icons_heart_handshake)

@Deprecated("LikeOutline has been renamed HeartOutline", ReplaceWith("SparkIcons.HeartOutline"))
public val SparkIcons.LikeOutline: DrawableRes get() = HeartOutline
public val SparkIcons.HeartOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_heart_outline)
public val SparkIcons.HomeCheckFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_home_check_fill)
public val SparkIcons.HouseQuestionCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_house_question_criteria)

@Deprecated("IdentityFill has been renamed IdentityCardFill", ReplaceWith("SparkIcons.IdentityCardFill"))
public val SparkIcons.IdentityFill: DrawableRes get() = IdentityCardFill
public val SparkIcons.IdentityCardFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_identity_card_fill)

@Deprecated("IdentityOutline has been renamed IdentityCardOutline", ReplaceWith("SparkIcons.IdentityCardOutline"))
public val SparkIcons.IdentityOutline: DrawableRes get() = IdentityCardOutline
public val SparkIcons.IdentityCardOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_identity_card_outline)

@Deprecated("InstagramFill has been renamed InstagramLogoFill", ReplaceWith("SparkIcons.InstagramLogoFill"))
public val SparkIcons.InstagramFill: DrawableRes get() = InstagramLogoFill
public val SparkIcons.InstagramLogoFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_instagram_logo_fill)

@Deprecated("InstagramOutline has been renamed InstagramLogoOutline", ReplaceWith("SparkIcons.InstagramLogoOutline"))
public val SparkIcons.InstagramOutline: DrawableRes get() = InstagramLogoOutline
public val SparkIcons.InstagramLogoOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_instagram_logo_outline)
public val SparkIcons.InterphoneCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_interphone_criteria)
public val SparkIcons.JewelsCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_jewels_criteria)
public val SparkIcons.KeyHouse: DrawableRes get() = DrawableRes(R.drawable.spark_icons_key_house)
public val SparkIcons.KeylessAccessCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_keyless_access_criteria)
public val SparkIcons.Lamp: DrawableRes get() = DrawableRes(R.drawable.spark_icons_lamp)

@Deprecated("Bump has been renamed LargeArrowUp", ReplaceWith("SparkIcons.LargeArrowUp"))
public val SparkIcons.Bump: DrawableRes get() = LargeArrowUp
public val SparkIcons.LargeArrowUp: DrawableRes get() = DrawableRes(R.drawable.spark_icons_large_arrow_up)
public val SparkIcons.LastFloorCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_last_floor_criteria)
public val SparkIcons.LayerFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_layer_fill)
public val SparkIcons.LayerOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_layer_outline)
public val SparkIcons.LetterDotFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_letter_dot_fill)
public val SparkIcons.LetterFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_letter_fill)
public val SparkIcons.LetterOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_letter_outline)
public val SparkIcons.LicenseCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_license_criteria)
public val SparkIcons.LiftCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_lift_criteria)

@Deprecated("IdeaFill has been renamed LightBulbFill", ReplaceWith("SparkIcons.LightBulbFill"))
public val SparkIcons.IdeaFill: DrawableRes get() = LightBulbFill
public val SparkIcons.LightBulbFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_light_bulb_fill)

@Deprecated("IdeaOutline has been renamed LightBulbOutline", ReplaceWith("SparkIcons.LightBulbOutline"))
public val SparkIcons.IdeaOutline: DrawableRes get() = LightBulbOutline
public val SparkIcons.LightBulbOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_light_bulb_outline)

@Deprecated("FlashFill has been renamed LightningFill", ReplaceWith("SparkIcons.LightningFill"))
public val SparkIcons.FlashFill: DrawableRes get() = LightningFill
public val SparkIcons.LightningFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_lightning_fill)

@Deprecated("LightningOutline has been renamed LightningOutline", ReplaceWith("SparkIcons.LightningOutline"))
public val SparkIcons.FlashOutline: DrawableRes get() = LightningOutline
public val SparkIcons.LightningOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_lightning_outline)
public val SparkIcons.LikeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_like_criteria)
public val SparkIcons.LinensCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_linens_criteria)

@Deprecated("Linkedin has been renamed LinkedinLogo", ReplaceWith("SparkIcons.LinkedinLogo"))
public val SparkIcons.Linkedin: DrawableRes get() = LinkedinLogo
public val SparkIcons.LinkedinLogo: DrawableRes get() = DrawableRes(R.drawable.spark_icons_linkedin_logo)

@Deprecated("Directory has been renamed List", ReplaceWith("SparkIcons.List"))
public val SparkIcons.Directory: DrawableRes get() = List
public val SparkIcons.List: DrawableRes get() = DrawableRes(R.drawable.spark_icons_list)
public val SparkIcons.ListingCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_listing_criteria)
public val SparkIcons.LocalisationCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_localisation_criteria)
public val SparkIcons.LockOutilne: DrawableRes get() = DrawableRes(R.drawable.spark_icons_lock_outilne)
public val SparkIcons.LockerOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_locker_outline)
public val SparkIcons.LoftCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_loft_criteria)
public val SparkIcons.LoggiaCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_loggia_criteria)
public val SparkIcons.LotusFlowerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_lotus_flower_criteria)
public val SparkIcons.Luggages: DrawableRes get() = DrawableRes(R.drawable.spark_icons_luggages)

@Deprecated("SearchFill has been renamed MagnifierFill", ReplaceWith("SparkIcons.MagnifierFill"))
public val SparkIcons.SearchFill: DrawableRes get() = MagnifierFill
public val SparkIcons.MagnifierFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_magnifier_fill)

@Deprecated("SearchOutline has been renamed MagnifierOutline", ReplaceWith("SparkIcons.MagnifierOutline"))
public val SparkIcons.SearchOutline: DrawableRes get() = MagnifierOutline
public val SparkIcons.MagnifierOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_magnifier_outline)
public val SparkIcons.MagnifierScan: DrawableRes get() = DrawableRes(R.drawable.spark_icons_magnifier_scan)

@Deprecated("MailCloseFill has been renamed MailBoxCloseFill", ReplaceWith("SparkIcons.MailBoxCloseFill"))
public val SparkIcons.MailCloseFill: DrawableRes get() = MailBoxCloseFill
public val SparkIcons.MailBoxCloseFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mail_box_close_fill)

@Deprecated("MailCloseOutline has been renamed MailBoxCloseOutline", ReplaceWith("SparkIcons.MailBoxCloseOutline"))
public val SparkIcons.MailCloseOutline: DrawableRes get() = MailBoxCloseOutline
public val SparkIcons.MailBoxCloseOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mail_box_close_outline)

@Deprecated("MailOpenFill has been renamed MailBoxOpenFill", ReplaceWith("SparkIcons.MailBoxOpenFill"))
public val SparkIcons.MailOpenFill: DrawableRes get() = MailBoxOpenFill
public val SparkIcons.MailBoxOpenFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mail_box_open_fill)

@Deprecated("MailOpenOutline has been renamed MailBoxOpenOutline", ReplaceWith("SparkIcons.MailBoxOpenOutline"))
public val SparkIcons.MailOpenOutline: DrawableRes get() = MailBoxOpenOutline
public val SparkIcons.MailBoxOpenOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mail_box_open_outline)
public val SparkIcons.ManUniformCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_man_uniform_criteria)

@Deprecated("WalkerFill has been renamed ManWalkingFill", ReplaceWith("SparkIcons.ManWalkingFill"))
public val SparkIcons.WalkerFill: DrawableRes get() = ManWalkingFill
public val SparkIcons.ManWalkingFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_man_walking_fill)

@Deprecated("WalkerOutline has been renamed ManWalkingOutline", ReplaceWith("SparkIcons.ManWalkingOutline"))
public val SparkIcons.WalkerOutline: DrawableRes get() = ManWalkingOutline
public val SparkIcons.ManWalkingOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_man_walking_outline)
public val SparkIcons.ManorHouseCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_manor_house_criteria)
public val SparkIcons.MaterialsCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_materials_criteria)
public val SparkIcons.Maternity: DrawableRes get() = DrawableRes(R.drawable.spark_icons_maternity)
public val SparkIcons.MeasuresCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_measures_criteria)

@Deprecated("Booster has been renamed Medal", ReplaceWith("SparkIcons.Medal"))
public val SparkIcons.Booster: DrawableRes get() = Medal
public val SparkIcons.Medal: DrawableRes get() = DrawableRes(R.drawable.spark_icons_medal)
public val SparkIcons.MedalCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_medal_criteria)

@Deprecated("Messenger has been renamed MessengerLogo", ReplaceWith("SparkIcons.MessengerLogo"))
public val SparkIcons.Messenger: DrawableRes get() = MessengerLogo
public val SparkIcons.MessengerLogo: DrawableRes get() = DrawableRes(R.drawable.spark_icons_messenger_logo)
public val SparkIcons.MeterCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_meter_criteria)
public val SparkIcons.MetroCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_metro_criteria)
public val SparkIcons.MicrochipPetCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_microchip_pet_criteria)

@Deprecated("VoiceFill has been renamed MicrophoneFill", ReplaceWith("SparkIcons.MicrophoneFill"))
public val SparkIcons.VoiceFill: DrawableRes get() = MicrophoneFill
public val SparkIcons.MicrophoneFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_microphone_fill)

@Deprecated("VoiceOffFill has been renamed MicrophoneOffFill", ReplaceWith("SparkIcons.MicrophoneOffFill"))
public val SparkIcons.VoiceOffFill: DrawableRes get() = MicrophoneOffFill
public val SparkIcons.MicrophoneOffFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_microphone_off_fill)

@Deprecated("VoiceOffOutline has been renamed MicrophoneOffOutline", ReplaceWith("SparkIcons.MicrophoneOffOutline"))
public val SparkIcons.VoiceOffOutline: DrawableRes get() = MicrophoneOffOutline
public val SparkIcons.MicrophoneOffOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_microphone_off_outline)

@Deprecated("VoiceOutline has been renamed MicrophoneOutline", ReplaceWith("SparkIcons.MicrophoneOutline"))
public val SparkIcons.VoiceOutline: DrawableRes get() = MicrophoneOutline
public val SparkIcons.MicrophoneOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_microphone_outline)
public val SparkIcons.Microwave: DrawableRes get() = DrawableRes(R.drawable.spark_icons_microwave)
public val SparkIcons.MileageCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mileage_criteria)

@Deprecated("RemoveFill has been renamed MinusCircleFill", ReplaceWith("SparkIcons.MinusCircleFill"))
public val SparkIcons.RemoveFill: DrawableRes get() = MinusCircleFill
public val SparkIcons.MinusCircleFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_minus_circle_fill)

@Deprecated("RemoveOutline has been renamed MinusCircleOutline", ReplaceWith("SparkIcons.MinusCircleOutline"))
public val SparkIcons.RemoveOutline: DrawableRes get() = MinusCircleOutline
public val SparkIcons.MinusCircleOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_minus_circle_outline)
public val SparkIcons.Mobile: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mobile)
public val SparkIcons.MobileCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mobile_criteria)
public val SparkIcons.MobileQuestionCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mobile_question_criteria)

@Deprecated("BankFill has been renamed MonumentFill", ReplaceWith("SparkIcons.MonumentFill"))
public val SparkIcons.BankFill: DrawableRes get() = MonumentFill
public val SparkIcons.MonumentFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_monument_fill)

@Deprecated("BankOutline has been renamed MonumentOutline", ReplaceWith("SparkIcons.MonumentOutline"))
public val SparkIcons.BankOutline: DrawableRes get() = MonumentOutline
public val SparkIcons.MonumentOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_monument_outline)
public val SparkIcons.MoonStar: DrawableRes get() = DrawableRes(R.drawable.spark_icons_moon_star)
public val SparkIcons.MotoCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_moto_criteria)
public val SparkIcons.MotorBike: DrawableRes get() = DrawableRes(R.drawable.spark_icons_motor_bike)
public val SparkIcons.MouldingCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_moulding_criteria)
public val SparkIcons.Mouse: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mouse)
public val SparkIcons.MouseComputerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_mouse_computer_criteria)
public val SparkIcons.Movie: DrawableRes get() = DrawableRes(R.drawable.spark_icons_movie)
public val SparkIcons.Music: DrawableRes get() = DrawableRes(R.drawable.spark_icons_music)
public val SparkIcons.Musician: DrawableRes get() = DrawableRes(R.drawable.spark_icons_musician)

@Deprecated("NeutralFill has been renamed NeutralFaceFill", ReplaceWith("SparkIcons.NeutralFaceFill"))
public val SparkIcons.NeutralFill: DrawableRes get() = NeutralFaceFill
public val SparkIcons.NeutralFaceFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_neutral_face_fill)

@Deprecated("NeutralOutline has been renamed NeutralFaceOutline", ReplaceWith("SparkIcons.NeutralFaceOutline"))
public val SparkIcons.NeutralOutline: DrawableRes get() = NeutralFaceOutline
public val SparkIcons.NeutralFaceOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_neutral_face_outline)
public val SparkIcons.NewRealEstate: DrawableRes get() = DrawableRes(R.drawable.spark_icons_new_real_estate)
public val SparkIcons.PaintPalette: DrawableRes get() = DrawableRes(R.drawable.spark_icons_paint_palette)
public val SparkIcons.PaperJobCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_paper_job_criteria)
public val SparkIcons.PaperPropertyCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_paper_property_criteria)

@Deprecated("HolidayFill has been renamed ParasolFill", ReplaceWith("SparkIcons.ParasolFill"))
public val SparkIcons.HolidayFill: DrawableRes get() = ParasolFill
public val SparkIcons.ParasolFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_parasol_fill)

@Deprecated("HolidayOutline has been renamed ParasolOutline", ReplaceWith("SparkIcons.ParasolOutline"))
public val SparkIcons.HolidayOutline: DrawableRes get() = ParasolOutline
public val SparkIcons.ParasolOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_parasol_outline)
public val SparkIcons.ParkAssistCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_park_assist_criteria)
public val SparkIcons.ParkingCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_parking_criteria)
public val SparkIcons.ParkingSensorCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_parking_sensor_criteria)
public val SparkIcons.ParquetCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_parquet_criteria)
public val SparkIcons.PawCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_paw_criteria)
public val SparkIcons.Pawn: DrawableRes get() = DrawableRes(R.drawable.spark_icons_pawn)
public val SparkIcons.PencilDocumentsCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_pencil_documents_criteria)
public val SparkIcons.PersonCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_person_criteria)
public val SparkIcons.PetAccessoryCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_pet_accessory_criteria)
public val SparkIcons.PetPaw: DrawableRes get() = DrawableRes(R.drawable.spark_icons_pet_paw)

@Deprecated("CallEnterFill has been renamed PhoneCallEnterFill", ReplaceWith("SparkIcons.PhoneCallEnterFill"))
public val SparkIcons.CallEnterFill: DrawableRes get() = PhoneCallEnterFill
public val SparkIcons.PhoneCallEnterFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_phone_call_enter_fill)

@Deprecated("CallEnterOutline has been renamed PhoneCallEnterOutline", ReplaceWith("SparkIcons.PhoneCallEnterOutline"))
public val SparkIcons.CallEnterOutline: DrawableRes get() = PhoneCallEnterOutline
public val SparkIcons.PhoneCallEnterOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_phone_call_enter_outline)

@Deprecated("CallFill has been renamed PhoneCallFill", ReplaceWith("SparkIcons.PhoneCallFill"))
public val SparkIcons.CallFill: DrawableRes get() = PhoneCallFill
public val SparkIcons.PhoneCallFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_phone_call_fill)

@Deprecated("CallMissFill has been renamed PhoneCallMissFill", ReplaceWith("SparkIcons.PhoneCallMissFill"))
public val SparkIcons.CallMissFill: DrawableRes get() = PhoneCallMissFill
public val SparkIcons.PhoneCallMissFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_phone_call_miss_fill)

@Deprecated("CallMissOutline has been renamed PhoneCallMissOutline", ReplaceWith("SparkIcons.PhoneCallMissOutline"))
public val SparkIcons.CallMissOutline: DrawableRes get() = PhoneCallMissOutline
public val SparkIcons.PhoneCallMissOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_phone_call_miss_outline)

@Deprecated("CallOutline has been renamed PhoneCallOutline", ReplaceWith("SparkIcons.PhoneCallOutline"))
public val SparkIcons.CallOutline: DrawableRes get() = PhoneCallOutline
public val SparkIcons.PhoneCallOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_phone_call_outline)
public val SparkIcons.PhotoFrameCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_photo_frame_criteria)

@Deprecated("Pinterest has been renamed PinterestLogo", ReplaceWith("SparkIcons.PinterestLogo"))
public val SparkIcons.Pinterest: DrawableRes get() = PinterestLogo
public val SparkIcons.PinterestLogo: DrawableRes get() = DrawableRes(R.drawable.spark_icons_pinterest_logo)
public val SparkIcons.Placeholder: DrawableRes get() = DrawableRes(R.drawable.spark_icons_placeholder)
public val SparkIcons.PlanCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_plan_criteria)
public val SparkIcons.Plant: DrawableRes get() = DrawableRes(R.drawable.spark_icons_plant)
public val SparkIcons.PlumbingElectricalCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_plumbing_electrical_criteria)
public val SparkIcons.PoolCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_pool_criteria)
public val SparkIcons.PowerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_power_criteria)

@Deprecated("PrintFill has been renamed PrinterFill", ReplaceWith("SparkIcons.PrinterFill"))
public val SparkIcons.PrintFill: DrawableRes get() = PrinterFill
public val SparkIcons.PrinterFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_printer_fill)

@Deprecated("PrintOutline has been renamed PrinterOutline", ReplaceWith("SparkIcons.PrinterOutline"))
public val SparkIcons.PrintOutline: DrawableRes get() = PrinterOutline
public val SparkIcons.PrinterOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_printer_outline)
public val SparkIcons.Pyramid: DrawableRes get() = DrawableRes(R.drawable.spark_icons_pyramid)
public val SparkIcons.RainSensorCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_rain_sensor_criteria)
public val SparkIcons.RearviewMirror: DrawableRes get() = DrawableRes(R.drawable.spark_icons_rearview_mirror)
public val SparkIcons.RearviewCameraCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_rearview_camera_criteria)

@Deprecated("Redo has been renamed RedoArrow", ReplaceWith("SparkIcons.RedoArrow"))
public val SparkIcons.Redo: DrawableRes get() = RedoArrow
public val SparkIcons.RedoArrow: DrawableRes get() = DrawableRes(R.drawable.spark_icons_redo_arrow)

@Deprecated("Refund has been renamed RefundEuro", ReplaceWith("SparkIcons.RefundEuro"))
public val SparkIcons.Refund: DrawableRes get() = RefundEuro
public val SparkIcons.RefundEuro: DrawableRes get() = DrawableRes(R.drawable.spark_icons_refund_euro)

@Deprecated("SadFill has been renamed SadFaceFill", ReplaceWith("SparkIcons.SadFaceFill"))
public val SparkIcons.SadFill: DrawableRes get() = SadFaceFill
public val SparkIcons.SadFaceFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sad_face_fill)

@Deprecated("SadOutline has been renamed SadFaceOutline", ReplaceWith("SparkIcons.SadFaceOutline"))
public val SparkIcons.SadOutline: DrawableRes get() = SadFaceOutline
public val SparkIcons.SadFaceOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sad_face_outline)
public val SparkIcons.Sailboat: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sailboat)

@Deprecated("UrgentFill has been renamed SandglassFill", ReplaceWith("SparkIcons.SandglassFill"))
public val SparkIcons.UrgentFill: DrawableRes get() = SandglassFill
public val SparkIcons.SandglassFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sandglass_fill)

@Deprecated("UrgentOutline has been renamed SandglassOutline", ReplaceWith("SparkIcons.SandglassOutline"))
public val SparkIcons.UrgentOutline: DrawableRes get() = SandglassOutline
public val SparkIcons.SandglassOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sandglass_outline)
public val SparkIcons.SchoolBag: DrawableRes get() = DrawableRes(R.drawable.spark_icons_school_bag)
public val SparkIcons.SeaViewCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sea_view_criteria)
public val SparkIcons.SeatCarCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_seat_car_criteria)
public val SparkIcons.SeatsCarCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_seats_car_criteria)
public val SparkIcons.SerringueCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_serringue_criteria)

@Deprecated("ShareExpand has been renamed ShareExternal", ReplaceWith("SparkIcons.ShareExternal"))
public val SparkIcons.ShareExpand: DrawableRes get() = ShareExternal
public val SparkIcons.ShareExternal: DrawableRes get() = DrawableRes(R.drawable.spark_icons_share_external)
public val SparkIcons.ShareiOs: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sharei_os)
public val SparkIcons.ShoesCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_shoes_criteria)
public val SparkIcons.ShovelRakeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_shovel_rake_criteria)
public val SparkIcons.ShowerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_shower_criteria)
public val SparkIcons.SimCardCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sim_card_criteria)
public val SparkIcons.SmokingCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_smoking_criteria)
public val SparkIcons.SnowflakeCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_snowflake_criteria)
public val SparkIcons.SortCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sort_criteria)
public val SparkIcons.SpeakerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_speaker_criteria)
public val SparkIcons.SpeedCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_speed_criteria)
public val SparkIcons.SpeedIndicatorCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_speed_indicator_criteria)
public val SparkIcons.SprayerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sprayer_criteria)
public val SparkIcons.Stack: DrawableRes get() = DrawableRes(R.drawable.spark_icons_stack)
public val SparkIcons.StairsCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_stairs_criteria)
public val SparkIcons.StarHouseCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_star_house_criteria)
public val SparkIcons.StarsCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_stars_criteria)
public val SparkIcons.StickerCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sticker_criteria)

@Deprecated("PauseFill has been renamed StopFill", ReplaceWith("SparkIcons.StopFill"))
public val SparkIcons.PauseFill: DrawableRes get() = StopFill
public val SparkIcons.StopFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_stop_fill)

@Deprecated("PauseOutline has been renamed StopOutline", ReplaceWith("SparkIcons.StopOutline"))
public val SparkIcons.PauseOutline: DrawableRes get() = StopOutline
public val SparkIcons.StopOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_stop_outline)
public val SparkIcons.StoreCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_store_criteria)

@Deprecated("NoPhoto has been renamed StrokeImage", ReplaceWith("SparkIcons.StrokeImage"))
public val SparkIcons.NoPhoto: DrawableRes get() = StrokeImage
public val SparkIcons.StrokeImage: DrawableRes get() = DrawableRes(R.drawable.spark_icons_stroke_image)
public val SparkIcons.Stroller: DrawableRes get() = DrawableRes(R.drawable.spark_icons_stroller)
public val SparkIcons.StudentHat: DrawableRes get() = DrawableRes(R.drawable.spark_icons_student_hat)
public val SparkIcons.StudentHatCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_student_hat_criteria)
public val SparkIcons.SunCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sun_criteria)
public val SparkIcons.SunMoon: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sun_moon)
public val SparkIcons.SunnyHouse: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sunny_house)
public val SparkIcons.SunroofCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_sunroof_criteria)
public val SparkIcons.Tablet: DrawableRes get() = DrawableRes(R.drawable.spark_icons_tablet)
public val SparkIcons.Tableware: DrawableRes get() = DrawableRes(R.drawable.spark_icons_tableware)

@Deprecated("OfferFill has been renamed TagFill", ReplaceWith("SparkIcons.TagFill"))
public val SparkIcons.OfferFill: DrawableRes get() = TagFill
public val SparkIcons.TagFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_tag_fill)

@Deprecated("OfferOutline has been renamed TagOutline", ReplaceWith("SparkIcons.TagOutline"))
public val SparkIcons.OfferOutline: DrawableRes get() = TagOutline
public val SparkIcons.TagOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_tag_outline)
public val SparkIcons.TeddyBear: DrawableRes get() = DrawableRes(R.drawable.spark_icons_teddy_bear)
public val SparkIcons.TerraceCriteria: DrawableRes get() = DrawableRes(R.drawable.spark_icons_terrace_criteria)

@Deprecated("ThreeD has been renamed ThreeDimension", ReplaceWith("SparkIcons.ThreeDimension"))
public val SparkIcons.ThreeD: DrawableRes get() = ThreeDimension
public val SparkIcons.ThreeDimension: DrawableRes get() = DrawableRes(R.drawable.spark_icons_three_dimension)

@Deprecated("MoreMenuVertical has been renamed ThreeDotsVertical", ReplaceWith("SparkIcons.ThreeDotsVertical"))
public val SparkIcons.MoreMenuVertical: DrawableRes get() = ThreeDotsVertical
public val SparkIcons.ThreeDotsHorizontal: DrawableRes get() = DrawableRes(R.drawable.spark_icons_three_dots_horizontal)

@Deprecated("MoreMenuHorizontal has been renamed ThreeDotsHorizontal", ReplaceWith("SparkIcons.ThreeDotsHorizontal"))
public val SparkIcons.MoreMenuHorizontal: DrawableRes get() = ThreeDotsHorizontal
public val SparkIcons.ThreeDotsVertical: DrawableRes get() = DrawableRes(R.drawable.spark_icons_three_dots_vertical)

@Deprecated("FavoriteFill has been renamed ThumbUpFill", ReplaceWith("SparkIcons.ThumbUpFill"))
public val SparkIcons.FavoriteFill: DrawableRes get() = ThumbUpFill
public val SparkIcons.ThumbUpFill: DrawableRes get() = DrawableRes(R.drawable.spark_icons_thumb_up_fill)

@Deprecated("FavoriteOutline has been renamed ThumbUpOutline", ReplaceWith("SparkIcons.ThumbUpOutline"))
public val SparkIcons.FavoriteOutline: DrawableRes get() = ThumbUpOutline
public val SparkIcons.ThumbUpOutline: DrawableRes get() = DrawableRes(R.drawable.spark_icons_thumb_up_outline)

@Deprecated("TopAd has been renamed Thumbtack", ReplaceWith("SparkIcons.Thumbtack"))
public val SparkIcons.TopAd: DrawableRes get() = Thumbtack
public val SparkIcons.Thumbtack: DrawableRes get() = DrawableRes(R.drawable.spark_icons_thumbtack)
public val SparkIcons.Ticket: DrawableRes get() = DrawableRes(R.drawable.spark_icons_ticket)
